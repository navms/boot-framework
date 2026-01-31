package io.navms.framework.common.idempotent.exception;

import io.navms.framework.common.base.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 幂等异常枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum IdempotentCode implements StatusCode {

    DO_NOT_REPEAT(10000, "请勿重复提交");

    private final Integer code;
    private final String message;

}
