package io.navms.framework.cache.utils;

import io.navms.framework.cache.exception.LockException;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.Requires;
import io.navms.framework.common.base.utils.SpringContextHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 基于 Redisson 的分布式锁工具类
 *
 * @author navms
 */
public abstract class LockUtil {

    /**
     * 获取锁对象
     */
    public static RLock getLock(String lockKey) {
        return getRedissonClient().getLock(lockKey);
    }

    /**
     * 获取公平锁
     */
    public static RLock getFairLock(String lockKey) {
        return getRedissonClient().getFairLock(lockKey);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey 锁key
     * @return 加锁结果
     */
    public static boolean tryLock(String lockKey) {
        RLock lock = getLock(lockKey);
        try {
            return lock.tryLock();
        } catch (Exception e) {
            LogUtils.error("Try lock failed, lockKey: {}", e, lockKey);
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey  锁key
     * @param waitTime 等待时间
     * @param unit     时间单位
     * @return 加锁结果
     */
    public static boolean tryLock(String lockKey, long waitTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        try {
            return lock.tryLock(waitTime, unit);
        } catch (Exception e) {
            LogUtils.error("Try lock failed, lockKey: {}, waitTime: {}", e, lockKey, waitTime);
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey   锁key
     * @param waitTime  等待时间
     * @param leaseTime 锁定时间
     * @param unit      时间单位
     * @return 加锁结果
     */
    public static boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (Exception e) {
            LogUtils.error("Try lock failed, lockKey: {}, waitTime: {}, leaseTime: {}", e,
                    lockKey, waitTime, leaseTime);
            return false;
        }
    }

    /**
     * 加锁
     *
     * @param lockKey 锁key
     */
    public static void lock(String lockKey) {
        RLock lock = getLock(lockKey);
        lock.lock();
    }

    /**
     * 加锁
     *
     * @param lockKey   锁key
     * @param leaseTime 锁定时间
     * @param unit      时间单位
     */
    public static void lock(String lockKey, long leaseTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        lock.lock(leaseTime, unit);
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁key
     */
    public static void unlock(String lockKey) {
        RLock lock = getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 强制释放锁（不检查线程持有）
     *
     * @param lockKey 锁key
     */
    public static void forceUnlock(String lockKey) {
        RLock lock = getLock(lockKey);
        if (lock.isLocked()) {
            lock.forceUnlock();
        }
    }

    /**
     * 检查锁是否被当前线程持有
     *
     * @param lockKey 锁key
     * @return 是否被当前线程持有
     */
    public static boolean isHeldByCurrentThread(String lockKey) {
        RLock lock = getLock(lockKey);
        return lock.isHeldByCurrentThread();
    }

    /**
     * 检查锁是否被锁定
     *
     * @param lockKey 锁key
     * @return 是否被锁定
     */
    public static boolean isLocked(String lockKey) {
        RLock lock = getLock(lockKey);
        return lock.isLocked();
    }

    /**
     * 执行带有锁的操作
     *
     * @param lockKey 锁key
     * @param task    待执行的任务
     */
    public static void executeWithLock(String lockKey, Runnable task) {
        executeWithLock(lockKey, task, -1, TimeUnit.SECONDS);
    }

    /**
     * 执行带有锁的操作
     *
     * @param lockKey 锁key
     * @param task    待执行的任务
     */
    public static void executeWithTryLock(String lockKey, Runnable task) {
        executeWithLock(lockKey, task, 0, TimeUnit.SECONDS);
    }

    /**
     * 执行带有锁的操作
     *
     * @param lockKey  锁key
     * @param task     待执行任务
     * @param waitTime 等待时间
     * @param unit     时间单位
     */
    public static void executeWithLock(String lockKey, Runnable task, long waitTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        boolean locked = false;
        try {
            if (waitTime >= 0) {
                locked = lock.tryLock(waitTime, unit);
            } else {
                lock.lock();
                locked = true;
            }

            if (locked) {
                task.run();
            } else {
                throw new LockException("Acquire lock timeout, lockKey: " + lockKey);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockException("Lock interrupted", e);
        } catch (Exception e) {
            throw new LockException("Execute with lock failed", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 执行带有锁的操作并返回结果
     *
     * @param lockKey 锁key
     * @param task    待执行的任务
     */
    public static <T> T executeWithLock(String lockKey, Supplier<T> task) {
        return executeWithLock(lockKey, task, -1, TimeUnit.SECONDS);
    }

    /**
     * 执行带有锁的操作并返回结果
     *
     * @param lockKey 锁key
     * @param task    待执行的任务
     */
    public static <T> T executeWithTryLock(String lockKey, Supplier<T> task) {
        return executeWithLock(lockKey, task, 0, TimeUnit.SECONDS);
    }

    /**
     * 执行带有锁的操作并返回结果
     *
     * @param lockKey  锁key
     * @param task     待执行任务
     * @param waitTime 等待时间
     * @param unit     时间单位
     */
    public static <T> T executeWithLock(String lockKey, Supplier<T> task, long waitTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        boolean locked = false;
        try {
            if (waitTime >= 0) {
                locked = lock.tryLock(waitTime, unit);
            } else {
                lock.lock();
                locked = true;
            }

            if (locked) {
                return task.get();
            } else {
                throw new LockException("Acquire lock timeout, lockKey: " + lockKey);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockException("Lock interrupted", e);
        } catch (Exception e) {
            throw new LockException("Execute with lock failed", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private static class RedissonClientHolder {
        private static final RedissonClient INSTANCE = Requires.requireNotNull(
                SpringContextHolder.getApplicationContext().getBean(RedissonClient.class), "缓存模块尚未加载");
    }

    public static RedissonClient getRedissonClient() {
        return RedissonClientHolder.INSTANCE;
    }

}