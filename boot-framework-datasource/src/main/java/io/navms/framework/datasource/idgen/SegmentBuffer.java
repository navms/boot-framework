package io.navms.framework.datasource.idgen;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 同一个 key 对应一个 SegmentBuffer，内部维护双 buffer
 *
 * @author navms
 */
@Data
public class SegmentBuffer {

    /**
     * 号段 Key
     */
    private String key;

    /**
     * 每个号段实际有两份
     */
    private Segment[] segments;

    /**
     * 当前的使用的 segment 的 index
     */
    private volatile int currentPos;

    /**
     * 下一个 segment 是否处于可切换状态
     */
    private volatile boolean nextReady;

    /**
     * 是否初始化完成
     */
    private volatile boolean init;

    /**
     * 下一个 buffer 是否加载中
     */
    private final AtomicBoolean nextLoading;

    /**
     * 读写锁
     */
    private final ReadWriteLock lock;

    /**
     * 缓存的步长
     */
    private volatile int step;

    /**
     * 步长最小值
     */
    private volatile int minStep;

    /**
     * buffer 更新时间，每一次从 db load 号段时，更新
     */
    private volatile long updateTimestamp;

    public SegmentBuffer(String key) {
        this.key = key;
        segments = new Segment[]{new Segment(this), new Segment(this)};
        currentPos = 0;
        nextReady = false;
        init = false;
        nextLoading = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    public Segment getCurrent() {
        return segments[currentPos];
    }

    public Segment getNext() {
        return segments[nextPos()];
    }

    public void initOk() {
        this.init = true;
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    public void switchPos() {
        currentPos = nextPos();
    }

    public void applyNewStep(int step, int minStep) {
        this.step = step;
        this.minStep = minStep;
        this.updateTimestamp = System.currentTimeMillis();
    }

    public IdGenResult tryGetValue() {
        Segment segment = this.getCurrent();
        long value = segment.getValue().getAndIncrement();
        if (value < segment.getMax()) {
            return IdGenResult.success(value);
        }
        return IdGenResult.failure(Status.FAILURE);
    }

}
