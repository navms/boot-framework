package io.navms.framework.common.ratelimit.annotation;

import io.navms.framework.common.ratelimit.enums.RateLimitAlgorithm;
import io.navms.framework.common.ratelimit.enums.RateLimitScene;
import io.navms.framework.common.ratelimit.enums.RateLimitType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 * 支持多种限流算法和限流类型
 *
 * @author navms
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流 key 前缀，用于区分不同的业务场景
     * 默认使用方法签名
     */
    String key() default "";

    /**
     * 限流时间窗口，默认 1 秒
     */
    long time() default 1;

    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流次数（令牌数量）
     */
    int count() default 100;

    /**
     * 限流类型
     */
    RateLimitType type() default RateLimitType.GLOBAL;

    /**
     * 限流算法
     */
    RateLimitAlgorithm algorithm() default RateLimitAlgorithm.SLIDING_WINDOW;

    /**
     * 限流场景
     */
    RateLimitScene scene() default RateLimitScene.WEB;

    /**
     * 自定义限流 key 的 SpEL 表达式
     * 仅在 type = CUSTOM 时有效
     * 示例：#userId, #request.userId, #user.id
     */
    String customKey() default "";

    /**
     * 失败时的提示信息
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * 是否启用降级（当 Redis 不可用时是否放行）
     */
    boolean fallback() default true;

}
