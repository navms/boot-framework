package io.navms.framework.common.base.log.aspect;

import java.lang.annotation.*;

/**
 * 日志注解
 * <p>
 * 用于标记需要记录操作日志的方法，可配置日志的标题、业务类型、是否记录参数、返回值等
 *
 * @author navms
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 业务类型
     */
    String bizType() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;

    /**
     * 是否记录返回值
     */
    boolean recordResult() default true;

    /**
     * 是否记录异常信息
     */
    boolean recordException() default true;

    /**
     * 是否异步记录日志（可用于数据库持久化等场景）
     */
    boolean async() default false;

    /**
     * 排除的参数索引（不记录的参数位置，从0开始）
     */
    int[] excludeParamIndexes() default {};

}
