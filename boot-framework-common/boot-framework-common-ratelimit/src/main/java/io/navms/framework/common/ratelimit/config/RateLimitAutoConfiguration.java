package io.navms.framework.common.ratelimit.config;

import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.ratelimit.aspect.RateLimitAspect;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategyFactory;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Common RateLimit 模块自动配置类
 *
 * @author navms
 */
@AutoConfiguration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = "boot.framework.ratelimit", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RateLimitAutoConfiguration {

    public RateLimitAutoConfiguration() {
        LogUtils.info("==============>>>>>>> Boot Framework Common RateLimit 模块已启用");
    }

    @Bean
    public RateLimitStrategyFactory rateLimitStrategyFactory() {
        return new RateLimitStrategyFactory();
    }

    @Bean
    public RateLimitAspect rateLimitAspect(RateLimitStrategyFactory strategyFactory,
                                           RateLimitProperties properties) {
        return new RateLimitAspect(strategyFactory, properties);
    }

}
