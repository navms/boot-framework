package io.navms.framework.common.idempotent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 幂等场景枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum IdempotentScene {

    /**
     * Web 场景
     */
    WEB,

    /**
     * MQ 场景
     */
    MQ,

    /**
     * RPC 场景
     */
    RPC

}
