package io.navms.framework.common.base.domain;

import io.navms.framework.common.base.enums.BaseCode;
import io.navms.framework.common.base.enums.StatusCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @author navms
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6797958946768271262L;

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return success(null, data);
    }

    public static <T> Result<T> success(String message) {
        return success(message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(BaseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> fail() {
        return fail(BaseCode.FAIL.getMessage());
    }

    public static <T> Result<T> fail(String message) {
        return fail(BaseCode.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(StatusCode statusCode) {
        return fail(statusCode, null);
    }

    public static <T> Result<T> fail(StatusCode statusCode, T data) {
        return new Result<>(statusCode.getCode(), statusCode.getMessage(), data);
    }

}
