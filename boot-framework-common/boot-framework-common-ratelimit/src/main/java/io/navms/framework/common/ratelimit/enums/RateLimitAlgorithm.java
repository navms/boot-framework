package io.navms.framework.common.ratelimit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限流算法枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum RateLimitAlgorithm {

    /**
     * 固定窗口算法
     * 特点：实现简单，但存在临界问题
     */
    FIXED_WINDOW("fixed_window", "固定窗口"),

    /**
     * 滑动窗口算法
     * 特点：精度更高，能平滑限流
     */
    SLIDING_WINDOW("sliding_window", "滑动窗口"),

    /**
     * 令牌桶算法
     * 特点：允许突发流量，平滑限流
     */
    TOKEN_BUCKET("token_bucket", "令牌桶"),

    /**
     * 漏桶算法
     * 特点：强制限流，流量整形
     */
    LEAKY_BUCKET("leaky_bucket", "漏桶");

    private final String code;
    private final String description;

}
