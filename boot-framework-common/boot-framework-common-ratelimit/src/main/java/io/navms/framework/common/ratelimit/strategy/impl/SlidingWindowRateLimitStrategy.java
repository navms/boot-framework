package io.navms.framework.common.ratelimit.strategy.impl;

import io.navms.framework.cache.utils.CacheUtil;
import io.navms.framework.common.base.utils.DateTimeUtils;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategy;
import org.redisson.api.RScoredSortedSet;

import java.util.concurrent.TimeUnit;

/**
 * 滑动窗口限流策略
 * 实现原理：使用有序集合（ZSet）存储请求时间戳，统计时间窗口内的请求数
 * 优点：精度高，能很好地平滑限流
 * 缺点：相比固定窗口，性能稍低，内存占用略高
 *
 * @author navms
 */
public class SlidingWindowRateLimitStrategy implements RateLimitStrategy {

    private static final String KEY_PREFIX = "rate_limit:sliding_window:";

    @Override
    public boolean tryAcquire(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RScoredSortedSet<String> sortedSet = CacheUtil.getRedissonClient().getScoredSortedSet(redisKey);

        long now = System.currentTimeMillis();
        long windowStart = now - timeUnit.toMillis(time);

        // 移除过期的请求记录
        sortedSet.removeRangeByScore(0, true, windowStart, false);

        // 获取当前窗口内的请求数
        int currentCount = sortedSet.size();

        if (currentCount < count) {
            // 添加当前请求
            sortedSet.add(now, String.valueOf(now));
            // 设置过期时间（窗口大小的2倍，确保数据及时清理）
            sortedSet.expire(DateTimeUtils.ofDuration(time * 2, timeUnit));
            return true;
        }

        return false;
    }

    @Override
    public long getRemaining(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RScoredSortedSet<String> sortedSet = CacheUtil.getRedissonClient().getScoredSortedSet(redisKey);

        long now = System.currentTimeMillis();
        long windowStart = now - timeUnit.toMillis(time);

        // 移除过期的请求记录
        sortedSet.removeRangeByScore(0, true, windowStart, false);

        // 获取当前窗口内的请求数
        int currentCount = sortedSet.size();
        long remaining = count - currentCount;

        return Math.max(0, remaining);
    }

}
