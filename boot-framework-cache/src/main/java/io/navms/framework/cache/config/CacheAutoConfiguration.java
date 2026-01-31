package io.navms.framework.cache.config;

import io.navms.framework.common.base.log.LogUtils;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * Cache 模块自动配置类
 *
 * @author navms
 */
@AutoConfiguration
public class CacheAutoConfiguration {

    public CacheAutoConfiguration() {
        LogUtils.info("==============>>>>>>> Boot Framework Cache 模块已启用");
    }

    @Bean
    @ConditionalOnClass(RedissonAutoConfigurationCustomizer.class)
    public RedissonAutoConfigurationCustomizer redissonClientConfiguration() {
        return config -> {
            config.setCodec(new JsonJacksonCodec());
            LogUtils.info("加载 Redisson 配置: {}, 使用 JSON 序列化", config);
        };
    }

}
