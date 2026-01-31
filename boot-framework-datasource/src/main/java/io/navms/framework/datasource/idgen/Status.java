package io.navms.framework.datasource.idgen;

import io.navms.framework.common.base.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拿号结果
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum Status implements StatusCode {

    SUCCESS(0, "成功"),
    FAILURE(-1, "在当前号段获取 Id 失败"),
    NOT_INIT(-2, "尚未初始化"),
    KEY_NOT_FOUND(-3, "号段键不存在"),
    EXCEPTION_ID_TWO_SEGMENTS_ARE_NULL(-4, "两个 Segment 均未装载");

    private final Integer code;
    private final String message;

}