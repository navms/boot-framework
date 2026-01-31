package io.navms.framework.common.idempotent.annotation;

import io.navms.framework.common.idempotent.enums.IdempotentScene;

import java.lang.annotation.*;

/**
 * 幂等注解
 * 核心原理：一锁二判三更新
 *
 * @author navms
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 间隔时间，单位毫秒
     */
    long period() default 3000;

    /**
     * 是否针对单个客户端幂等
     */
    boolean perClient() default true;

    /**
     * 自定义幂等请求头
     */
    String[] headers() default {};

    /**
     * 幂等场景
     */
    IdempotentScene scene() default IdempotentScene.WEB;

}
