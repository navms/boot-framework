package io.navms.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * 范围查询注解
 * <p>
 * 标注在查询 DTO 的字段上，表示该字段使用 BETWEEN 查询（WHERE column BETWEEN value1 AND value2）
 * <p>
 * 字段类型应该是数组（长度为2）或 List（包含2个元素），例如：Long[] 或 List&lt;Long&gt;
 * <p>
 * 如果只提供一个值，则转换为 >= 查询
 *
 * @author navms
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Between {

    /**
     * 对应的数据库字段名（实体类属性名）
     * <p>
     * 如果不指定，则使用注解标注的字段名
     */
    String column() default "";

}
