package io.navms.framework.common.base.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * 线程上下文工具类
 *
 * @author navms
 */
public abstract class ThreadLocalUtils {

    private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL_MAP = new InheritableThreadLocal<>();

    private static final int INITIAL_CAPACITY = 8;

    public static Map<Object, Object> getThreadLocalMap() {
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        if (map == null) {
            map = new HashMap<>(INITIAL_CAPACITY);
            THREAD_LOCAL_MAP.set(map);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key) {
        Requires.requireNotNull(key, "key cannot be null");
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        return map == null ? null : (T) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key, T defaultValue) {
        Requires.requireNotNull(key, "key cannot be null");
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        if (map == null) {
            return defaultValue;
        }
        T value = (T) map.get(key);
        return value != null ? value : defaultValue;
    }

    public static void set(Object key, Object value) {
        Requires.requireNotNull(key, "key cannot be null");
        Map<Object, Object> map = getThreadLocalMap();
        map.put(key, value);
    }

    public static void set(Map<Object, Object> keyValueMap) {
        Requires.requireNotNull(keyValueMap, "keyValueMap cannot be null");
        if (!keyValueMap.isEmpty()) {
            Map<Object, Object> map = getThreadLocalMap();
            map.putAll(keyValueMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<Object, T> getValuesByPrefix(String prefix) {
        if (StrUtil.isEmpty(prefix)) {
            return Collections.emptyMap();
        }

        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        if (MapUtil.isEmpty(map)) {
            return Collections.emptyMap();
        }

        Map<Object, T> result = new HashMap<>();
        map.forEach((key, value) -> {
            if (key != null && key.toString().startsWith(prefix)) {
                result.put(key, (T) value);
            }
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T remove(Object key) {
        Requires.requireNotNull(key, "key cannot be null");
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        return map == null ? null : (T) map.remove(key);
    }

    public static void clearByPrefix(String prefix) {
        if (StrUtil.isEmpty(prefix)) {
            return;
        }

        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        if (MapUtil.isEmpty(map)) {
            return;
        }

        // 使用迭代器安全删除
        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            if (key != null && key.toString().startsWith(prefix)) {
                iterator.remove();
            }
        }
    }

    public static Set<Object> keys() {
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        return map == null ? Collections.emptySet() : map.keySet();
    }

    public static boolean containsKey(Object key) {
        Requires.requireNotNull(key, "key cannot be null");
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        return map != null && map.containsKey(key);
    }

    public static int size() {
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        return map == null ? 0 : map.size();
    }

    public static boolean isEmpty() {
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        return map == null || map.isEmpty();
    }

    public static void clear() {
        Map<Object, Object> map = THREAD_LOCAL_MAP.get();
        if (map != null) {
            map.clear();
        }
        THREAD_LOCAL_MAP.remove();
    }

    public static void setString(String key, String value) {
        set(key, value);
    }

    public static String getString(String key) {
        return get(key);
    }

    public static String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    public static void setInt(String key, Integer value) {
        set(key, value);
    }

    public static Integer getInt(String key) {
        return get(key);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        return get(key, defaultValue);
    }

    public static void setLong(String key, Long value) {
        set(key, value);
    }

    public static Long getLong(String key) {
        return get(key);
    }

    public static Long getLong(String key, Long defaultValue) {
        return get(key, defaultValue);
    }

}