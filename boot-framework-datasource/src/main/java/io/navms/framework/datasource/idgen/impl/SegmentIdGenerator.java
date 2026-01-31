package io.navms.framework.datasource.idgen.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.CollectionUtils;
import io.navms.framework.datasource.idgen.*;
import io.navms.framework.datasource.idgen.entity.IdSegment;
import io.navms.framework.datasource.idgen.mapper.IdSegmentMapper;
import org.springframework.beans.factory.DisposableBean;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 号段模式
 * <p>注意在数据库创建 id_segment 表</p>
 *
 * @author navms
 */
public class SegmentIdGenerator implements IdGenerator, DisposableBean {

    private volatile boolean init = false;

    private final Map<String, SegmentBuffer> bufferCache = new ConcurrentHashMap<>();

    private final IdSegmentMapper idSegmentMapper;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 反映号段使用速度的时间间隔
     */
    private static final long SEGMENT_DURATION = 15 * 60 * 1000L;

    /**
     * 最大步长不要超过 100,0000
     */
    private static final int MAX_STEP = 1000000;

    /**
     * 默认步长
     */
    private static final int DEFAULT_STEP = 1000;

    /**
     * 默认初始化大小
     */
    private static final long DEFAULT_MAX_ID = 1000;

    private final ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new LoadThreadFactory());

    public SegmentIdGenerator(IdSegmentMapper idSegmentMapper) {
        this.idSegmentMapper = idSegmentMapper;
    }

    public static class LoadThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Segment-Load-" + nextThreadNum());
        }
    }

    @Override
    public IdGenResult nextId(String key) {
        if (!init) {
            return IdGenResult.failure(Status.NOT_INIT);
        }
        if (bufferCache.containsKey(key)) {
            SegmentBuffer buffer = bufferCache.get(key);
            if (!buffer.isInit()) {
                synchronized (buffer) {
                    if (!buffer.isInit()) {
                        try {
                            loadSegment(key, buffer.getCurrent());
                            LogUtils.debug("init buffer. update segment key {} {} from db.", key, buffer.getCurrent());
                            buffer.initOk();
                        } catch (Exception ex) {
                            LogUtils.error("init buffer {} exception", buffer.getCurrent(), ex);
                        }
                    }
                }
            }
            return nextIdFromSegmentBuffer(bufferCache.get(key));
        }
        return IdGenResult.failure(Status.KEY_NOT_FOUND);
    }

    private IdGenResult nextIdFromSegmentBuffer(SegmentBuffer buffer) {
        while (true) {
            buffer.rLock().lock();
            try {
                Segment segment = buffer.getCurrent();
                if (!buffer.isNextReady() && (segment.getIdle() < 0.9 * segment.getStep()) &&
                        buffer.getNextLoading().compareAndSet(false, true)) {
                    service.execute(() -> {
                        Segment next = buffer.getNext();
                        boolean updateOk = false;
                        try {
                            loadSegment(buffer.getKey(), next);
                            updateOk = true;
                            LogUtils.info("update segment {} from db {}", buffer.getKey(), next);
                        } catch (Exception e) {
                            LogUtils.error(buffer.getKey() + " update segment from db exception", e);
                        } finally {
                            if (updateOk) {
                                buffer.wLock().lock();
                                buffer.setNextReady(true);
                                buffer.getNextLoading().set(false);
                                buffer.wLock().unlock();
                            } else {
                                buffer.getNextLoading().set(false);
                            }
                        }
                    });
                }
                IdGenResult result = buffer.tryGetValue();
                if (result.isSuccess()) {
                    return result;
                }
            } finally {
                buffer.rLock().unlock();
            }
            waitAndSleep(buffer);
            buffer.wLock().lock();
            try {
                IdGenResult result = buffer.tryGetValue();
                if (result.isSuccess()) {
                    return result;
                }
                if (buffer.isNextReady()) {
                    buffer.switchPos();
                    buffer.setNextReady(false);
                } else {
                    LogUtils.error("both two segments in {} are not ready!", buffer);
                    return IdGenResult.failure(Status.EXCEPTION_ID_TWO_SEGMENTS_ARE_NULL);
                }
            } finally {
                buffer.wLock().unlock();
            }
        }
    }

    private void waitAndSleep(SegmentBuffer buffer) {
        int roll = 0;
        while (buffer.getNextLoading().get()) {
            roll += 1;
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                LogUtils.warn("Thread {} Interrupted", Thread.currentThread().getName());
                break;
            }
            if (roll > 100) {
                return;
            }
        }
    }

    @Override
    public void init() {
        this.init = initializeIdSegments() && refreshBufferCache() && startScheduledTask();
    }

    private boolean initializeIdSegments() {
        try {
            List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
            if (CollectionUtils.isEmpty(tableInfos)) {
                return true;
            }

            // 获取数据库中已存在的 keys
            List<String> existingKeys = Optional.ofNullable(idSegmentMapper.selectAllKeys()).orElse(Collections.emptyList());
            Set<String> existingKeySet = new HashSet<>(existingKeys);

            // 遍历所有子类，检查并插入缺失的记录
            int insertCount = 0;
            List<IdSegment> segments = new ArrayList<>();

            Set<? extends Class<?>> doClass = tableInfos.stream().map(TableInfo::getEntityType).collect(Collectors.toSet());
            LogUtils.info("Scanning doClass {} ...", doClass);

            for (Class<?> clazz : doClass) {
                if (clazz.isAssignableFrom(IdSegment.class)) {
                    continue;
                }
                String className = clazz.getSimpleName();
                if (!existingKeySet.contains(className)) {
                    IdSegment segment = new IdSegment();
                    segment.setKey(className);
                    segment.setMaxId(DEFAULT_MAX_ID);
                    segment.setStep(DEFAULT_STEP);
                    segment.setDescription("Auto-generated for " + clazz.getSimpleName());
                    segments.add(segment);

                    insertCount++;
                    LogUtils.info("Auto-inserted id_segment record for key: {}", className);
                }
            }

            if (insertCount > 0) {
                LogUtils.info("Auto-initialized id_segment records: {}", segments);
                idSegmentMapper.insertBatchSomeColumn(segments);
            } else {
                LogUtils.info("All BaseDO subclasses already have id_segment records");
            }

            return true;
        } catch (Exception ex) {
            LogUtils.error("Failed to initialize id_segments", ex);
            return false;
        }
    }

    private boolean startScheduledTask() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("refresh-cache-thread");
            t.setDaemon(true);
            return t;
        });
        scheduledExecutorService.scheduleWithFixedDelay(this::refreshBufferCache, 0, 5, TimeUnit.MINUTES);
        return true;
    }

    private boolean refreshBufferCache() {
        try {
            List<String> keys = idSegmentMapper.selectAllKeys();
            if (CollectionUtils.isEmpty(keys)) {
                return true;
            }

            Set<String> cacheKeys = new HashSet<>(bufferCache.keySet());
            Set<String> dbKeys = new HashSet<>(keys);

            Collection<String> keysToAdd = CollectionUtils.subtract(dbKeys, cacheKeys);
            Collection<String> keysToRemove = CollectionUtils.subtract(cacheKeys, dbKeys);

            keysToAdd.forEach(key -> {
                SegmentBuffer buffer = new SegmentBuffer(key);
                bufferCache.put(key, buffer);
                LogUtils.info("add key {} from db to IdCache, SegmentBuffer {}", key, buffer);
            });

            keysToRemove.forEach(bufferCache::remove);
            LogUtils.info("remove keys {} from IdCache", keysToRemove);
        } catch (Exception ex) {
            LogUtils.error("refresh cache from db exception", ex);
            return false;
        }
        return true;
    }

    // 一定是单线程执行的
    private void loadSegment(String key, Segment segment) {
        SegmentBuffer buffer = segment.getBuffer();
        if (!buffer.isInit()) {
            idSegmentMapper.updateMaxId(key);
            IdSegment idSegment = idSegmentMapper.selectByKey(key);
            buffer.applyNewStep(idSegment.getStep(), idSegment.getStep());
            segment.applyToSegment(idSegment);
            return;
        }

        int newStep = this.calculateNextStep(buffer);
        idSegmentMapper.updateMaxIdByStep(key, newStep);
        IdSegment idSegment = idSegmentMapper.selectByKey(key);
        buffer.applyNewStep(newStep, idSegment.getStep());
        segment.applyToSegment(idSegment);
    }

    /**
     * 根据号段使用耗时调整下一个 step 大小
     *
     * @param buffer 缓存的 buffer
     * @return 下一个 step 大小
     */
    private int calculateNextStep(SegmentBuffer buffer) {
        long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
        int currentStep = buffer.getStep();
        int nextStep = currentStep;

        if (duration < SEGMENT_DURATION) {
            // 消耗太快 → 扩大 step
            nextStep = Math.min(currentStep * 2, MAX_STEP);
        } else if (duration >= SEGMENT_DURATION * 2) {
            // 消耗太慢 → 缩小 step
            nextStep = Math.max(currentStep / 2, buffer.getMinStep());
        }

        LogUtils.info("leafKey[{}], step[{}], duration[{} mins], nextStep[{}]", buffer.getKey(),
                currentStep, String.format("%.2f", (double) duration / (1000 * 60)), nextStep);
        return nextStep;
    }

    @Override
    public void destroy() throws Exception {
        scheduledExecutorService.close();
    }

}
