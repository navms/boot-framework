package io.navms.framework.cache.exception;


import io.navms.framework.common.base.exception.BaseException;

import java.io.Serial;

/**
 * 锁异常
 *
 * @author navms
 */
public class LockException extends BaseException {

    @Serial
    private static final long serialVersionUID = 3847612775207433305L;

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }
}
