package io.navms.framework.datasource.exception;

import io.navms.framework.common.base.enums.StatusCode;
import io.navms.framework.common.base.exception.BusinessException;

import java.io.Serial;

/**
 * ID 生成异常
 *
 * @author navms
 */
public class IdGenerateException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 6159642834628174871L;

    public IdGenerateException(StatusCode statusCode) {
        super(statusCode);
    }

}
