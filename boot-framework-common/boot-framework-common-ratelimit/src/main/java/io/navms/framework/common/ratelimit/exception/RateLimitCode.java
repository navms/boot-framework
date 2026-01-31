package io.navms.framework.common.ratelimit.exception;

import io.navms.framework.common.base.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限流异常状态码枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum RateLimitCode implements StatusCode {

    /**
     * 限流异常
     */
    RATE_LIMIT_EXCEEDED(20000, "访问过于频繁，请稍后再试"),

    /**
     * 限流配置异常
     */
    RATE_LIMIT_CONFIG_ERROR(20001, "限流配置错误"),

    /**
     * 限流服务异常
     */
    RATE_LIMIT_SERVICE_ERROR(20002, "限流服务异常");

    private final Integer code;
    private final String message;

}
