package io.navms.framework.common.ratelimit.strategy.impl;

import io.navms.framework.cache.utils.CacheUtil;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategy;
import org.redisson.api.RScript;
import org.redisson.client.codec.LongCodec;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶限流策略
 * 实现原理：请求以任意速率进入桶，以固定速率从桶中流出
 * 优点：强制限流，能够平滑流量
 * 缺点：无法应对突发流量
 * <p>
 * 使用 Lua 脚本实现原子性操作
 *
 * @author navms
 */
public class LeakyBucketRateLimitStrategy implements RateLimitStrategy {

    private static final String KEY_PREFIX = "rate_limit:leaky_bucket:";

    /**
     * Lua 脚本实现漏桶算法
     * KEYS[1]: 限流 key
     * ARGV[1]: 桶容量（最大请求数）
     * ARGV[2]: 漏出速率（每秒处理的请求数）
     * ARGV[3]: 当前时间戳（毫秒）
     * ARGV[4]: 时间窗口（毫秒）
     * <p>
     * 返回值：1 表示成功，0 表示失败
     */
    private static final String LEAKY_BUCKET_SCRIPT =
            """
                    local key = KEYS[1]
                    local capacity = tonumber(ARGV[1])
                    local rate = tonumber(ARGV[2])
                    local now = tonumber(ARGV[3])
                    local window = tonumber(ARGV[4])
                    
                    local bucket = redis.call('hgetall', key)
                    local last_time = now
                    local water = 0
                    
                    -- 解析桶状态
                    if #bucket > 0 then
                        for i = 1, #bucket, 2 do
                            if bucket[i] == 'last_time' then
                                last_time = tonumber(bucket[i + 1])
                            elseif bucket[i] == 'water' then
                                water = tonumber(bucket[i + 1])
                            end
                        end
                    end
                    
                    -- 计算漏出的水量
                    local leaked = math.floor((now - last_time) / 1000 * rate)
                    water = math.max(0, water - leaked)
                    
                    -- 尝试加水
                    if water < capacity then
                        water = water + 1
                        redis.call('hset', key, 'water', water, 'last_time', now)
                        redis.call('pexpire', key, window)
                        return 1
                    else
                        return 0
                    end
                    """;

    @Override
    public boolean tryAcquire(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;
        RScript script = CacheUtil.getRedissonClient().getScript(LongCodec.INSTANCE);

        long now = System.currentTimeMillis();
        long windowMillis = timeUnit.toMillis(time);
        // 计算漏出速率（每秒处理的请求数）
        double rate = (double) count / timeUnit.toSeconds(time);

        Long result = script.eval(
                RScript.Mode.READ_WRITE,
                LEAKY_BUCKET_SCRIPT,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(redisKey),
                count,       // 桶容量
                rate,        // 漏出速率
                now,         // 当前时间
                windowMillis // 过期时间
        );

        return result != null && result == 1L;
    }

    @Override
    public long getRemaining(String key, int count, long time, TimeUnit timeUnit) {
        String redisKey = KEY_PREFIX + key;

        // 获取桶中的水量
        Long water = CacheUtil.getRedissonClient().<Long>getBucket(redisKey + ":water").get();
        if (water == null) {
            return count;
        }

        long remaining = count - water;
        return Math.max(0, remaining);
    }

}
