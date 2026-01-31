package io.navms.framework.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础响应码枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum BaseCode implements StatusCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    REQUEST_TIMEOUT(408, "请求超时"),
    VALIDATE_FAILED(422, "参数校验失败"),
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final Integer code;
    private final String message;

}

