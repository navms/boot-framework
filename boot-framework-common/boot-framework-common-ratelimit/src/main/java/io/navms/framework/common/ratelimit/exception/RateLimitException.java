package io.navms.framework.common.ratelimit.exception;

import io.navms.framework.common.base.enums.StatusCode;
import io.navms.framework.common.base.exception.BaseException;

import java.io.Serial;

/**
 * 限流异常
 *
 * @author navms
 */
public class RateLimitException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3726891234567890123L;

    public RateLimitException(String message) {
        super(RateLimitCode.RATE_LIMIT_EXCEEDED.getCode(), message);
    }

    public RateLimitException(StatusCode statusCode) {
        super(statusCode.getCode(), statusCode.getMessage());
    }

    public RateLimitException(Integer code, String message) {
        super(code, message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(RateLimitCode.RATE_LIMIT_EXCEEDED.getCode(), message, cause);
    }

}
