package io.navms.framework.common.ratelimit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限流类型枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum RateLimitType {

    /**
     * 全局限流（针对接口）
     */
    GLOBAL("global", "全局限流"),

    /**
     * IP 限流（针对同一 IP）
     */
    IP("ip", "IP限流"),

    /**
     * 用户限流（针对同一用户）
     */
    USER("user", "用户限流"),

    /**
     * 自定义限流（基于 SpEL 表达式）
     */
    CUSTOM("custom", "自定义限流");

    private final String code;
    private final String description;

}
