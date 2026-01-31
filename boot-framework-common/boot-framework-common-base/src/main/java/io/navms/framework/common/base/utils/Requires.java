package io.navms.framework.common.base.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 参数校验工具类
 *
 * @author navms
 */
public abstract class Requires {

    public static <T> T requireNotNull(T obj) {
        return requireNotNull(obj, "参数不能为空");
    }

    public static <T> T requireNotNull(T obj, String message) {
        return requireNotNull(obj, () -> new IllegalArgumentException(message));
    }

    public static <T> T requireNotNull(T obj, Supplier<RuntimeException> supplier) {
        if (obj == null) {
            throw supplier.get();
        }
        return obj;
    }

    public static String requireNotEmpty(String str) {
        return requireNotEmpty(str, "字符串不能为空");
    }

    public static String requireNotEmpty(String str, String message) {
        return requireNotEmpty(str, () -> new IllegalArgumentException(message));
    }

    public static String requireNotEmpty(String str, Supplier<RuntimeException> supplier) {
        if (StrUtil.isEmpty(str)) {
            throw supplier.get();
        }
        return str;
    }

    public static <T extends Collection<?>> T requireNotEmpty(T collection) {
        return requireNotEmpty(collection, "集合不能为空");
    }

    public static <T extends Collection<?>> T requireNotEmpty(T collection, String message) {
        return requireNotEmpty(collection, () -> new IllegalArgumentException(message));
    }

    public static <T extends Collection<?>> T requireNotEmpty(T collection, Supplier<RuntimeException> supplier) {
        if (collection == null || collection.isEmpty()) {
            throw supplier.get();
        }
        return collection;
    }

    public static <T extends Map<?, ?>> T requireNotEmpty(T map) {
        return requireNotEmpty(map, "Map不能为空");
    }

    public static <T extends Map<?, ?>> T requireNotEmpty(T map, String message) {
        return requireNotEmpty(map, () -> new IllegalArgumentException(message));
    }

    public static <T extends Map<?, ?>> T requireNotEmpty(T map, Supplier<RuntimeException> supplier) {
        if (map == null || map.isEmpty()) {
            throw supplier.get();
        }
        return map;
    }

    public static <T> T[] requireNotEmpty(T[] array) {
        return requireNotEmpty(array, "数组不能为空");
    }

    public static <T> T[] requireNotEmpty(T[] array, String message) {
        return requireNotEmpty(array, () -> new IllegalArgumentException(message));
    }

    public static <T> T[] requireNotEmpty(T[] array, Supplier<RuntimeException> supplier) {
        if (array == null || array.length == 0) {
            throw supplier.get();
        }
        return array;
    }

    public static void requireTrue(boolean condition) {
        requireTrue(condition, "条件必须为真");
    }

    public static void requireTrue(boolean condition, String message) {
        requireTrue(condition, () -> new IllegalArgumentException(message));
    }

    public static void requireTrue(boolean condition, Supplier<RuntimeException> supplier) {
        if (!condition) {
            throw supplier.get();
        }
    }

}
