package io.navms.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * IN 查询注解
 * <p>
 * 标注在查询 DTO 的字段上，表示该字段使用 IN 查询（WHERE column IN (value1, value2, ...)）
 * <p>
 * 字段类型应该是 Collection 或数组
 * 
 * @author navms
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface In {

    /**
     * 对应的数据库字段名（实体类属性名）
     * <p>
     * 如果不指定，则使用注解标注的字段名
     */
    String column() default "";

}
