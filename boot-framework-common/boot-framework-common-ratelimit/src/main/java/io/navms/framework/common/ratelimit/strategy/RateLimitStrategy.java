package io.navms.framework.common.ratelimit.strategy;

import java.util.concurrent.TimeUnit;

/**
 * 限流策略接口
 * 定义限流算法的核心方法
 *
 * @author navms
 */
public interface RateLimitStrategy {

    /**
     * 尝试获取令牌
     *
     * @param key      限流 key
     * @param count    限流次数（令牌数量）
     * @param time     时间窗口
     * @param timeUnit 时间单位
     * @return true 获取成功，false 获取失败（被限流）
     */
    boolean tryAcquire(String key, int count, long time, TimeUnit timeUnit);

    /**
     * 获取剩余配额
     *
     * @param key      限流 key
     * @param count    限流次数
     * @param time     时间窗口
     * @param timeUnit 时间单位
     * @return 剩余配额，-1 表示无法获取
     */
    long getRemaining(String key, int count, long time, TimeUnit timeUnit);

}
