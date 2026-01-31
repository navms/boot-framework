package io.navms.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * 模糊查询注解
 * <p>
 * 标注在查询 DTO 的字段上，表示该字段使用模糊查询（WHERE column LIKE '%value%'）
 * 
 * @author navms
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Like {

    /**
     * 对应的数据库字段名（实体类属性名）
     * <p>
     * 如果不指定，则使用注解标注的字段名
     */
    String column() default "";

    /**
     * 是否左匹配（%value）
     */
    boolean left() default false;

    /**
     * 是否右匹配（value%）
     */
    boolean right() default false;

}
