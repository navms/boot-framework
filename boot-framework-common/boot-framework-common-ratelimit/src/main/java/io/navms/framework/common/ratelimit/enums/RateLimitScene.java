package io.navms.framework.common.ratelimit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限流场景枚举
 *
 * @author navms
 */
@Getter
@AllArgsConstructor
public enum RateLimitScene {

    /**
     * Web 接口场景
     */
    WEB,

    /**
     * RPC 调用场景
     */
    RPC,

    /**
     * MQ 消费场景
     */
    MQ,

    /**
     * 定时任务场景
     */
    SCHEDULED

}
