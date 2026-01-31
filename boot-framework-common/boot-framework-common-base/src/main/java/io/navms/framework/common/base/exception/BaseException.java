package io.navms.framework.common.base.exception;

import io.navms.framework.common.base.enums.BaseCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 基础异常
 *
 * @author navms
 */
@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2280410055064447746L;

    private final Integer code;
    private final String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
        this.code = BaseCode.FAIL.getCode();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = BaseCode.FAIL.getCode();
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}

