package io.navms.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * 排序注解
 * <p>
 * 标注在查询 DTO 的字段上，表示根据该字段进行排序
 * <p>
 * 字段值为 Boolean 类型：true 表示启用排序，false 或 null 表示不排序
 * 
 * @author navms
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderBy {

    /**
     * 对应的数据库字段名（实体类属性名）
     * <p>
     * 如果不指定，则使用注解标注的字段名
     */
    String column() default "";

    /**
     * 排序方向：true 为升序（ASC），false 为降序（DESC）
     */
    boolean asc() default true;

    /**
     * 排序优先级，数字越小优先级越高
     */
    int priority() default 0;

}
