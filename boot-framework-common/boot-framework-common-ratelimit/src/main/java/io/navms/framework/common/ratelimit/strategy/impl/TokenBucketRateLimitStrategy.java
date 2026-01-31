package io.navms.framework.common.ratelimit.strategy.impl;

import io.navms.framework.cache.utils.CacheUtil;
import io.navms.framework.common.base.utils.DateTimeUtils;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategy;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;

import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流策略
 * 实现原理：以恒定速率向桶中添加令牌，请求时从桶中获取令牌
 * 优点：允许一定的突发流量，平滑限流
 * 缺点：实现相对复杂
 * <p>
 * 使用 Redisson 内置的 RateLimiter 实现
 *
 * @author navms
 */
public class TokenBucketRateLimitStrategy implements RateLimitStrategy {

    private static final String KEY_PREFIX = "rate_limit:token_bucket:";

    @Override
    public boolean tryAcquire(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RRateLimiter rateLimiter = CacheUtil.getRedissonClient().getRateLimiter(redisKey);

        // 如果限流器未初始化，则初始化
        if (!rateLimiter.isExists()) {
            // 配置令牌桶：rate = count / time
            // RateType.OVERALL 表示全局限流
            rateLimiter.trySetRate(RateType.OVERALL, count, DateTimeUtils.ofDuration(time, timeUnit));
        }

        // 尝试获取1个令牌
        return rateLimiter.tryAcquire(1);
    }

    @Override
    public long getRemaining(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RRateLimiter rateLimiter = CacheUtil.getRedissonClient().getRateLimiter(redisKey);

        if (!rateLimiter.isExists()) {
            return count;
        }

        // Redisson 的 RateLimiter 不直接提供剩余令牌数的查询
        // 这里返回可用令牌数（近似值）
        return rateLimiter.availablePermits();
    }

}
