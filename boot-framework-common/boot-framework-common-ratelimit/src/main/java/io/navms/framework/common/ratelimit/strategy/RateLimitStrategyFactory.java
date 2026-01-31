package io.navms.framework.common.ratelimit.strategy;

import io.navms.framework.common.base.utils.Requires;
import io.navms.framework.common.ratelimit.enums.RateLimitAlgorithm;
import io.navms.framework.common.ratelimit.exception.RateLimitCode;
import io.navms.framework.common.ratelimit.exception.RateLimitException;
import io.navms.framework.common.ratelimit.strategy.impl.FixedWindowRateLimitStrategy;
import io.navms.framework.common.ratelimit.strategy.impl.LeakyBucketRateLimitStrategy;
import io.navms.framework.common.ratelimit.strategy.impl.SlidingWindowRateLimitStrategy;
import io.navms.framework.common.ratelimit.strategy.impl.TokenBucketRateLimitStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 限流策略工厂
 * 负责创建和管理不同的限流策略实例
 *
 * @author navms
 */
public class RateLimitStrategyFactory {

    private final Map<RateLimitAlgorithm, RateLimitStrategy> strategyMap = new HashMap<>();

    public RateLimitStrategyFactory() {
        initStrategies();
    }

    /**
     * 初始化所有限流策略
     */
    private void initStrategies() {
        strategyMap.put(RateLimitAlgorithm.FIXED_WINDOW, new FixedWindowRateLimitStrategy());
        strategyMap.put(RateLimitAlgorithm.SLIDING_WINDOW, new SlidingWindowRateLimitStrategy());
        strategyMap.put(RateLimitAlgorithm.TOKEN_BUCKET, new TokenBucketRateLimitStrategy());
        strategyMap.put(RateLimitAlgorithm.LEAKY_BUCKET, new LeakyBucketRateLimitStrategy());
    }

    /**
     * 获取限流策略
     *
     * @param algorithm 限流算法
     * @return 限流策略实例
     */
    public RateLimitStrategy getStrategy(RateLimitAlgorithm algorithm) {
        RateLimitStrategy strategy = strategyMap.get(algorithm);
        return Requires.requireNotNull(strategy, () -> new RateLimitException(RateLimitCode.RATE_LIMIT_CONFIG_ERROR));
    }

}
