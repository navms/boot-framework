package io.navms.framework.common.base.exception;

import io.navms.framework.common.base.enums.BaseCode;
import io.navms.framework.common.base.enums.StatusCode;
import io.navms.framework.common.base.utils.StringUtils;
import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author navms
 */
@Getter
public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = -1419271164788518016L;

    public BusinessException(String message) {
        super(BaseCode.FAIL.getCode(), message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(String message, Throwable cause) {
        super(BaseCode.FAIL.getCode(), message, cause);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BusinessException(StatusCode statusCode) {
        super(statusCode.getCode(), statusCode.getMessage());
    }

    public BusinessException(StatusCode statusCode, Throwable cause) {
        super(statusCode.getCode(), statusCode.getMessage(), cause);
    }

    public BusinessException(StatusCode statusCode, Object... args) {
        super(statusCode.getCode(), StringUtils.format(statusCode.getMessage(), args));
    }

    public BusinessException(StatusCode statusCode, Throwable cause, Object... args) {
        super(statusCode.getCode(), StringUtils.format(statusCode.getMessage(), args), cause);
    }

}

