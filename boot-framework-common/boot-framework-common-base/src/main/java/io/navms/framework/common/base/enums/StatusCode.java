package io.navms.framework.common.base.enums;

/**
 * 状态码接口
 *
 * @author navms
 */
public interface StatusCode {

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    Integer getCode();

    /**
     * 获取状态码描述
     *
     * @return 状态码描述
     */
    String getMessage();

}