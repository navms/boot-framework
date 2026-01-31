package io.navms.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * 查询注解 - 通用查询条件注解
 * <p>
 * 可以标注在查询 DTO 的字段上，自动构建 MyBatis-Plus 查询条件
 *
 * @author navms
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {

    /**
     * 查询类型
     */
    QueryType type() default QueryType.EQ;

    /**
     * 对应的数据库字段名
     * <p>
     * 如果不指定，则使用注解标注的字段名
     */
    String column() default "";

    /**
     * 查询类型枚举
     */
    enum QueryType {
        /**
         * 等于 =
         */
        EQ,

        /**
         * 不等于 !=
         */
        NE,

        /**
         * 大于 >
         */
        GT,

        /**
         * 大于等于 >=
         */
        GE,

        /**
         * 小于 <
         */
        LT,

        /**
         * 小于等于 <=
         */
        LE,

        /**
         * 模糊查询 LIKE
         */
        LIKE,

        /**
         * 左模糊查询 LIKE '%value'
         */
        LEFT_LIKE,

        /**
         * 右模糊查询 LIKE 'value%'
         */
        RIGHT_LIKE,

        /**
         * IN 查询
         */
        IN,

        /**
         * NOT IN 查询
         */
        NOT_IN,

        /**
         * BETWEEN 查询（字段值需要是数组或 List，包含两个元素）
         */
        BETWEEN,

        /**
         * IS NULL
         */
        IS_NULL,

        /**
         * IS NOT NULL
         */
        IS_NOT_NULL
    }

}
