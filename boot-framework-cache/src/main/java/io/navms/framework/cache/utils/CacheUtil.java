package io.navms.framework.cache.utils;

import io.navms.framework.common.base.utils.Requires;
import io.navms.framework.common.base.utils.SpringContextHolder;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.KeysScanParams;
import org.redisson.client.codec.StringCodec;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * 基于 Redisson 的缓存工具类
 *
 * @author navms
 */
public abstract class CacheUtil {

    // ==================== 基础对象操作 ====================

    /**
     * 设置缓存对象
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param duration 过期时间
     */
    public static <T> void setObject(String key, T value, Duration duration) {
        RBucket<T> bucket = getRedissonClient().getBucket(key);
        if (duration != null) {
            bucket.set(value, duration);
        } else {
            bucket.set(value);
        }
    }

    /**
     * 获取缓存对象
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public static <T> T getObject(String key) {
        RBucket<T> bucket = getRedissonClient().getBucket(key);
        return bucket.get();
    }

    // ==================== 字符串操作 ====================

    /**
     * 设置字符串值
     *
     * @param key      缓存键
     * @param value    字符串值
     * @param duration 过期时间
     */
    public static void setString(String key, String value, Duration duration) {
        RBucket<String> bucket = getRedissonClient().getBucket(key, StringCodec.INSTANCE);
        if (duration != null) {
            bucket.set(value, duration);
        } else {
            bucket.set(value);
        }
    }

    /**
     * 获取字符串值
     *
     * @param key 缓存键
     * @return 字符串值
     */
    public static String getString(String key) {
        RBucket<String> bucket = getRedissonClient().getBucket(key, StringCodec.INSTANCE);
        return bucket.get();
    }

    // ==================== Map 操作 ====================

    /**
     * 向 Map 中存入一个键值对。
     * 如果 Map 不存在，则会自动创建。
     *
     * @param mapKey 整个 Map 的缓存键
     * @param key    Map 中的键
     * @param value  Map 中的值
     * @param <K>    Map 键的类型
     * @param <V>    Map 值的类型
     */
    public static <K, V> void put(String mapKey, K key, V value) {
        RMap<K, V> map = getRedissonClient().getMap(mapKey);
        map.put(key, value);
    }

    /**
     * 向 Map 中存入一个键值对，并设置整个 Map 的过期时间。
     * 如果 Map 不存在，则会自动创建。
     *
     * @param mapKey   整个 Map 的缓存键
     * @param key      Map 中的键
     * @param value    Map 中的值
     * @param duration Map 的过期时间
     * @param <K>      Map 键的类型
     * @param <V>      Map 值的类型
     */
    public static <K, V> void put(String mapKey, K key, V value, Duration duration) {
        RMap<K, V> map = getRedissonClient().getMap(mapKey);
        map.put(key, value);
        if (duration != null) {
            map.expire(duration);
        }
    }

    /**
     * 批量向 Map 中存入键值对。
     *
     * @param mapKey 整个 Map 的缓存键
     * @param m      包含多个键值对的 Map
     * @param <K>    Map 键的类型
     * @param <V>    Map 值的类型
     */
    public static <K, V> void putAll(String mapKey, Map<? extends K, ? extends V> m) {
        RMap<K, V> map = getRedissonClient().getMap(mapKey);
        map.putAll(m);
    }

    /**
     * 从 Map 中获取指定键的值。
     *
     * @param mapKey 整个 Map 的缓存键
     * @param key    Map 中的键
     * @param <K>    Map 键的类型
     * @param <V>    Map 值的类型
     * @return Map 中该键对应的值，如果键不存在则返回 null
     */
    public static <K, V> V getMap(String mapKey, K key) {
        RMap<K, V> map = getRedissonClient().getMap(mapKey);
        return map.get(key);
    }

    /**
     * 获取整个 Map 对象。
     *
     * @param mapKey 整个 Map 的缓存键
     * @param <K>    Map 键的类型
     * @param <V>    Map 值的类型
     * @return 一个包含 Map 所有键值对的普通 Java Map
     */
    public static <K, V> Map<K, V> getMap(String mapKey) {
        RMap<K, V> map = getRedissonClient().getMap(mapKey);
        return map.readAllMap();
    }

    /**
     * 从 Map 中删除指定的键。
     *
     * @param mapKey 整个 Map 的缓存键
     * @param key    Map 中的键
     * @param <K>    Map 键的类型
     * @param <V>    Map 值的类型
     * @return 被删除的键所对应的值，如果键不存在则返回 null
     */
    public static <K, V> V remove(String mapKey, K key) {
        RMap<K, V> map = getRedissonClient().getMap(mapKey);
        return map.remove(key);
    }

    /**
     * 检查 Map 中是否包含指定的键。
     *
     * @param mapKey 整个 Map 的缓存键
     * @param key    Map 中的键
     * @param <K>    Map 键的类型
     * @return 如果包含则返回 true，否则返回 false
     */
    public static <K> boolean containsKey(String mapKey, K key) {
        RMap<K, ?> map = getRedissonClient().getMap(mapKey);
        return map.containsKey(key);
    }

    /**
     * 获取 Map 的大小（键值对的数量）。
     *
     * @param mapKey 整个 Map 的缓存键
     * @return Map 的大小
     */
    public static long getMapSize(String mapKey) {
        RMap<?, ?> map = getRedissonClient().getMap(mapKey);
        return map.size();
    }

    /**
     * 清空 Map 中的所有键值对，但保留 Map 本身。
     *
     * @param mapKey 整个 Map 的缓存键
     */
    public static void clearMap(String mapKey) {
        RMap<?, ?> map = getRedissonClient().getMap(mapKey);
        map.clear();
    }

    /**
     * 删除整个 Map（包括其所有键值对）。
     *
     * @param mapKey 整个 Map 的缓存键
     * @return 是否删除成功
     */
    public static boolean deleteMap(String mapKey) {
        return getRedissonClient().getMap(mapKey).delete();
    }

    /**
     * 为整个 Map 设置过期时间。
     *
     * @param mapKey   整个 Map 的缓存键
     * @param duration 过期时间
     * @return 是否设置成功
     */
    public static boolean expireMap(String mapKey, Duration duration) {
        if (duration == null) {
            return false;
        }
        return getRedissonClient().getMap(mapKey).expire(duration);
    }

    // ==================== 其他实用操作 ====================

    /**
     * 如果键不存在则设置值
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param duration 过期时间
     * @return 是否设置成功
     */
    public static <T> boolean setIfAbsent(String key, T value, Duration duration) {
        RBucket<T> bucket = getRedissonClient().getBucket(key);
        if (duration != null) {
            return bucket.setIfAbsent(value, duration);
        }
        return bucket.setIfAbsent(value);
    }

    /**
     * 删除缓存键
     *
     * @param key 缓存键
     * @return 是否删除成功
     */
    public static boolean delete(String key) {
        return getRedissonClient().getBucket(key).delete();
    }

    /**
     * 检查键是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    public static boolean exists(String key) {
        return getRedissonClient().getBucket(key).isExists();
    }

    /**
     * 设置过期时间
     *
     * @param key      缓存键
     * @param duration 过期时间
     * @return 是否设置成功
     */
    public static boolean expire(String key, Duration duration) {
        if (duration == null) {
            return false;
        }
        return getRedissonClient().getBucket(key).expire(duration);
    }

    /**
     * 获取剩余存活时间
     *
     * @param key 缓存键
     * @return 剩余时间（毫秒）, -2表示键不存在, -1表示键存在但无过期时间
     */
    public static long getTimeToLive(String key) {
        return getRedissonClient().getBucket(key).remainTimeToLive();
    }

    /**
     * 原子递增
     *
     * @param key 计数器键
     * @return 递增后的值
     */
    public static long increment(String key) {
        return getRedissonClient().getAtomicLong(key).incrementAndGet();
    }

    /**
     * 原子递减
     *
     * @param key 计数器键
     * @return 递减后的值
     */
    public static long decrement(String key) {
        return getRedissonClient().getAtomicLong(key).decrementAndGet();
    }

    /**
     * 发布消息
     *
     * @param channel 频道
     * @param message 消息
     */
    public static <T> void publish(String channel, T message) {
        getRedissonClient().getTopic(channel).publish(message);
    }

    /**
     * 扫描匹配的键
     *
     * @param pattern 匹配模式
     * @return 匹配的键列表
     */
    public static Collection<String> scan(String pattern) {
        RKeys rKeys = getRedissonClient().getKeys();
        KeysScanParams options = new KeysScanParams();
        options.pattern(pattern);
        return rKeys.getKeysStream(options).toList();
    }

    // ==================== 内部方法 ====================

    private static class RedissonClientHolder {
        private static final RedissonClient INSTANCE = Requires.requireNotNull(
                SpringContextHolder.getApplicationContext().getBean(RedissonClient.class), "缓存模块尚未加载");
    }

    public static RedissonClient getRedissonClient() {
        return RedissonClientHolder.INSTANCE;
    }

}