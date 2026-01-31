package io.navms.framework.common.ratelimit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 限流配置属性
 *
 * @author navms
 */
@Data
@ConfigurationProperties(prefix = "boot.framework.ratelimit")
public class RateLimitProperties {

    /**
     * 是否启用限流
     */
    private boolean enabled = true;

    /**
     * 全局限流 key 前缀
     */
    private String keyPrefix = "rate_limit";

    /**
     * 是否启用降级（当 Redis 不可用时是否放行）
     */
    private boolean fallback = true;

    /**
     * 是否打印限流日志
     */
    private boolean logEnabled = true;

}
