package io.navms.framework.common.ratelimit.strategy.impl;

import io.navms.framework.cache.utils.CacheUtil;
import io.navms.framework.common.base.utils.DateTimeUtils;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategy;
import org.redisson.api.RAtomicLong;

import java.util.concurrent.TimeUnit;

/**
 * 固定窗口限流策略
 * 实现原理：在固定时间窗口内限制请求次数
 * 优点：实现简单，性能高
 * 缺点：存在临界问题（窗口边界时可能出现瞬时流量超过限制）
 *
 * @author navms
 */
public class FixedWindowRateLimitStrategy implements RateLimitStrategy {

    private static final String KEY_PREFIX = "rate_limit:fixed_window:";

    @Override
    public boolean tryAcquire(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RAtomicLong atomicLong = CacheUtil.getRedissonClient().getAtomicLong(redisKey);

        long current = atomicLong.incrementAndGet();
        // 首次访问，设置过期时间
        if (current == 1) {
            atomicLong.expire(DateTimeUtils.ofDuration(time, timeUnit));
        }

        return current <= count;
    }

    @Override
    public long getRemaining(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RAtomicLong atomicLong = CacheUtil.getRedissonClient().getAtomicLong(redisKey);

        long current = atomicLong.get();
        long remaining = count - current;

        return Math.max(0, remaining);
    }

}
