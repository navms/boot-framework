package io.navms.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * 小于等于查询注解
 * <p>
 * 标注在查询 DTO 的字段上，表示该字段使用小于等于查询（WHERE column <= value）
 * 
 * @author navms
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Le {

    /**
     * 对应的数据库字段名（实体类属性名）
     * <p>
     * 如果不指定，则使用注解标注的字段名
     */
    String column() default "";

}
