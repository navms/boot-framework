package io.navms.framework.datasource.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.CollectionUtils;
import io.navms.framework.common.base.utils.StringUtils;
import io.navms.framework.datasource.annotation.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询条件构建器
 * <p>
 * 根据 DTO 字段上的查询注解自动构建 MyBatis-Plus 的 QueryWrapper
 * <p>
 * 支持的注解：@Query、@Eq、@Like、@In、@Gt、@Ge、@Lt、@Le、@Between、@OrderBy
 * <p>
 * 使用示例：
 * <pre>
 * UserQueryDTO query = new UserQueryDTO();
 * query.setUsername("admin");
 * query.setStatus(1);
 *
 * QueryWrapper&lt;User&gt; wrapper = QueryWrapperBuilder.build(query, User.class);
 * List&lt;User&gt; users = userMapper.selectList(wrapper);
 * </pre>
 *
 * @author navms
 */
public class QueryWrapperBuilder {

    /**
     * 字段缓存，避免重复反射
     */
    private static final Map<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据查询对象构建 QueryWrapper
     *
     * @param query 查询对象
     * @param <T>   实体类型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> build(Object query) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (query == null) {
            return wrapper;
        }

        // 获取所有字段（包括父类字段）
        List<Field> fields = getAllFields(query.getClass());

        // 排序字段列表（用于处理 @OrderBy）
        List<OrderByField> orderByFields = new ArrayList<>();

        // 遍历字段，处理查询注解
        for (Field field : fields) {
            try {
                // 设置字段可访问
                field.setAccessible(true);

                // 获取字段值
                Object value = field.get(query);

                // 处理 @Query 注解
                if (field.isAnnotationPresent(Query.class)) {
                    handleQueryAnnotation(wrapper, field, value);
                }

                // 处理 @Eq 注解
                if (field.isAnnotationPresent(Eq.class)) {
                    handleEqAnnotation(wrapper, field, value);
                }

                // 处理 @Like 注解
                if (field.isAnnotationPresent(Like.class)) {
                    handleLikeAnnotation(wrapper, field, value);
                }

                // 处理 @In 注解
                if (field.isAnnotationPresent(In.class)) {
                    handleInAnnotation(wrapper, field, value);
                }

                // 处理 @Gt 注解
                if (field.isAnnotationPresent(Gt.class)) {
                    handleGtAnnotation(wrapper, field, value);
                }

                // 处理 @Ge 注解
                if (field.isAnnotationPresent(Ge.class)) {
                    handleGeAnnotation(wrapper, field, value);
                }

                // 处理 @Lt 注解
                if (field.isAnnotationPresent(Lt.class)) {
                    handleLtAnnotation(wrapper, field, value);
                }

                // 处理 @Le 注解
                if (field.isAnnotationPresent(Le.class)) {
                    handleLeAnnotation(wrapper, field, value);
                }

                // 处理 @Between 注解
                if (field.isAnnotationPresent(Between.class)) {
                    handleBetweenAnnotation(wrapper, field, value);
                }

                // 处理 @OrderBy 注解
                if (field.isAnnotationPresent(OrderBy.class)) {
                    handleOrderByAnnotation(orderByFields, field, value);
                }

            } catch (IllegalAccessException e) {
                LogUtils.warn("Failed to access field: {}", field.getName(), e);
            }
        }

        // 处理排序
        applyOrderBy(wrapper, orderByFields);

        return wrapper;
    }

    /**
     * 处理 @Query 注解
     */
    private static <T> void handleQueryAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Query annotation = field.getAnnotation(Query.class);
        String column = getColumnName(annotation.column(), field.getName());
        Query.QueryType type = annotation.type();

        switch (type) {
            case EQ -> applyEq(wrapper, column, value);
            case NE -> applyNe(wrapper, column, value);
            case GT -> applyGt(wrapper, column, value);
            case GE -> applyGe(wrapper, column, value);
            case LT -> applyLt(wrapper, column, value);
            case LE -> applyLe(wrapper, column, value);
            case LIKE -> applyLike(wrapper, column, value, false, false);
            case LEFT_LIKE -> applyLike(wrapper, column, value, true, false);
            case RIGHT_LIKE -> applyLike(wrapper, column, value, false, true);
            case IN -> applyIn(wrapper, column, value);
            case NOT_IN -> applyNotIn(wrapper, column, value);
            case BETWEEN -> applyBetween(wrapper, column, value);
            case IS_NULL -> applyIsNull(wrapper, column, value, true);
            case IS_NOT_NULL -> applyIsNull(wrapper, column, value, false);
        }
    }

    /**
     * 处理 @Eq 注解
     */
    private static <T> void handleEqAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Eq annotation = field.getAnnotation(Eq.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyEq(wrapper, column, value);
    }

    /**
     * 处理 @Like 注解
     */
    private static <T> void handleLikeAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Like annotation = field.getAnnotation(Like.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyLike(wrapper, column, value, annotation.left(), annotation.right());
    }

    /**
     * 处理 @In 注解
     */
    private static <T> void handleInAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        In annotation = field.getAnnotation(In.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyIn(wrapper, column, value);
    }

    /**
     * 处理 @Gt 注解
     */
    private static <T> void handleGtAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Gt annotation = field.getAnnotation(Gt.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyGt(wrapper, column, value);
    }

    /**
     * 处理 @Ge 注解
     */
    private static <T> void handleGeAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Ge annotation = field.getAnnotation(Ge.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyGe(wrapper, column, value);
    }

    /**
     * 处理 @Lt 注解
     */
    private static <T> void handleLtAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Lt annotation = field.getAnnotation(Lt.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyLt(wrapper, column, value);
    }

    /**
     * 处理 @Le 注解
     */
    private static <T> void handleLeAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Le annotation = field.getAnnotation(Le.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyLe(wrapper, column, value);
    }

    /**
     * 处理 @Between 注解
     */
    private static <T> void handleBetweenAnnotation(QueryWrapper<T> wrapper, Field field, Object value) {
        Between annotation = field.getAnnotation(Between.class);
        String column = getColumnName(annotation.column(), field.getName());
        applyBetween(wrapper, column, value);
    }

    /**
     * 处理 @OrderBy 注解
     */
    private static void handleOrderByAnnotation(List<OrderByField> orderByFields, Field field, Object value) {
        // 如果字段值为 null 或 false，则不排序
        if (value == null || (value instanceof Boolean && !((Boolean) value))) {
            return;
        }

        OrderBy annotation = field.getAnnotation(OrderBy.class);
        String column = getColumnName(annotation.column(), field.getName());
        orderByFields.add(new OrderByField(column, annotation.asc(), annotation.priority()));
    }

    /**
     * 应用 EQ 查询条件
     */
    private static <T> void applyEq(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.eq(StringUtils.toUnderlineCase(column), value);
        }
    }

    /**
     * 应用 NE 查询条件
     */
    private static <T> void applyNe(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.ne(StringUtils.toUnderlineCase(column), value);
        }
    }

    /**
     * 应用 GT 查询条件
     */
    private static <T> void applyGt(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.gt(StringUtils.toUnderlineCase(column), value);
        }
    }

    /**
     * 应用 GE 查询条件
     */
    private static <T> void applyGe(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.ge(StringUtils.toUnderlineCase(column), value);
        }
    }

    /**
     * 应用 LT 查询条件
     */
    private static <T> void applyLt(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.lt(StringUtils.toUnderlineCase(column), value);
        }
    }

    /**
     * 应用 LE 查询条件
     */
    private static <T> void applyLe(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.le(StringUtils.toUnderlineCase(column), value);
        }
    }

    /**
     * 应用模糊查询条件
     */
    private static <T> void applyLike(QueryWrapper<T> wrapper, String column, Object value,
                                      boolean leftLike, boolean rightLike) {
        if (!(value instanceof String str) || StringUtils.isEmpty(str)) {
            return;
        }

        String columnName = StringUtils.toUnderlineCase(column);
        if (leftLike) {
            wrapper.likeLeft(columnName, str);
        } else if (rightLike) {
            wrapper.likeRight(columnName, str);
        } else {
            wrapper.like(columnName, str);
        }
    }

    /**
     * 应用 IN 查询条件
     */
    private static <T> void applyIn(QueryWrapper<T> wrapper, String column, Object value) {
        Collection<?> collection = convertToCollection(value);
        if (CollectionUtils.isNotEmpty(collection)) {
            wrapper.in(StringUtils.toUnderlineCase(column), collection);
        }
    }

    /**
     * 应用 NOT IN 查询条件
     */
    private static <T> void applyNotIn(QueryWrapper<T> wrapper, String column, Object value) {
        Collection<?> collection = convertToCollection(value);
        if (CollectionUtils.isNotEmpty(collection)) {
            wrapper.notIn(StringUtils.toUnderlineCase(column), collection);
        }
    }

    /**
     * 应用 BETWEEN 查询条件
     */
    private static <T> void applyBetween(QueryWrapper<T> wrapper, String column, Object value) {
        if (value == null) {
            return;
        }

        String columnName = StringUtils.toUnderlineCase(column);
        Object val1 = null;
        Object val2 = null;

        // 处理数组
        if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            if (array.length > 0) val1 = array[0];
            if (array.length > 1) val2 = array[1];
        }
        // 处理 List
        else if (value instanceof List<?> list) {
            if (!list.isEmpty()) val1 = list.get(0);
            if (list.size() > 1) val2 = list.get(1);
        }

        // 应用条件
        if (val1 != null && val2 != null) {
            wrapper.between(columnName, val1, val2);
        } else if (val1 != null) {
            wrapper.ge(columnName, val1);
        } else if (val2 != null) {
            wrapper.le(columnName, val2);
        }
    }

    /**
     * 应用 IS NULL / IS NOT NULL 查询条件
     */
    private static <T> void applyIsNull(QueryWrapper<T> wrapper, String column, Object value, boolean isNull) {
        // 如果字段值为 null 或 false，则不应用条件
        if (value == null || (value instanceof Boolean && !((Boolean) value))) {
            return;
        }

        String columnName = StringUtils.toUnderlineCase(column);
        if (isNull) {
            wrapper.isNull(columnName);
        } else {
            wrapper.isNotNull(columnName);
        }
    }

    /**
     * 应用排序
     */
    private static <T> void applyOrderBy(QueryWrapper<T> wrapper, List<OrderByField> orderByFields) {
        if (CollectionUtils.isEmpty(orderByFields)) {
            return;
        }

        // 按优先级排序
        orderByFields.sort(Comparator.comparingInt(OrderByField::priority));

        for (OrderByField orderByField : orderByFields) {
            String columnName = StringUtils.toUnderlineCase(orderByField.column());
            wrapper.orderBy(true, orderByField.asc(), columnName);
        }
    }

    /**
     * 获取字段名（如果注解指定了 column，则使用 column，否则使用字段名）
     */
    private static String getColumnName(String annotationColumn, String fieldName) {
        return StringUtils.isNotEmpty(annotationColumn) ? annotationColumn : fieldName;
    }

    /**
     * 将值转换为集合
     */
    private static Collection<?> convertToCollection(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Collection) {
            return (Collection<?>) value;
        }

        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        }

        return Collections.singletonList(value);
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> cachedFields = FIELD_CACHE.get(clazz);
        if (cachedFields != null) {
            return cachedFields;
        }

        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        FIELD_CACHE.put(clazz, fields);
        return fields;
    }

    /**
     * 排序字段
     */
    private record OrderByField(String column, boolean asc, int priority) {
    }

}
