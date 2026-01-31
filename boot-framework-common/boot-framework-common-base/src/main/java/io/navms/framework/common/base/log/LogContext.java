package io.navms.framework.common.base.log;

import io.navms.framework.common.base.constant.Constants;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

/**
 * 日志上下文 用于存储和传递请求链路信息
 *
 * @author navms
 */
public abstract class LogContext {

    /**
     * TRACE_ID
     */
    public static final String TRACE_ID = Constants.TRACE_ID;

    /**
     * USER_ID
     */
    public static final String USER_ID = Constants.USER_ID;

    /**
     * TENANT_ID
     */
    public static final String TENANT_ID = Constants.TENANT_ID;

    /**
     * CLIENT_IP
     */
    public static final String CLIENT_IP = Constants.CLIENT_IP;

    /**
     * REQUEST_URI
     */
    public static final String REQUEST_URI = Constants.REQUEST_URI;

    /**
     * 设置 traceId
     *
     * @param traceId traceId
     */
    public static void setTraceId(String traceId) {
        put(TRACE_ID, traceId);
    }

    /**
     * 获取 traceId
     *
     * @return traceId
     */
    public static String getTraceId() {
        return get(TRACE_ID);
    }

    /**
     * 移除 traceId
     */
    public static void removeTraceId() {
        remove(TRACE_ID);
    }

    /**
     * 生成并设置 traceId
     *
     * @return traceId
     */
    public static String generateTraceId() {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        setTraceId(traceId);
        return traceId;
    }

    /**
     * 设置 userId
     *
     * @param userId userId
     */
    public static void setUserId(String userId) {
        put(USER_ID, userId);
    }

    /**
     * 获取 userId
     *
     * @return userId
     */
    public static String getUserId() {
        return get(USER_ID);
    }

    /**
     * 移除 userId
     */
    public static void removeUserId() {
        remove(USER_ID);
    }

    /**
     * 设置 tenantId
     *
     * @param tenantId tenantId
     */
    public static void setTenantId(String tenantId) {
        put(TENANT_ID, tenantId);
    }

    /**
     * 获取 tenantId
     *
     * @return tenantId
     */
    public static String getTenantId() {
        return get(TENANT_ID);
    }

    /**
     * 移除 tenantId
     */
    public static void removeTenantId() {
        remove(TENANT_ID);
    }

    /**
     * 设置 clientIp
     *
     * @param clientIp clientIp
     */
    public static void setClientIp(String clientIp) {
        put(CLIENT_IP, clientIp);
    }

    /**
     * 获取 clientIp
     *
     * @return clientIp
     */
    public static String getClientIp() {
        return get(CLIENT_IP);
    }

    /**
     * 移除 clientIp
     */
    public static void removeClientIp() {
        remove(CLIENT_IP);
    }

    /**
     * 设置 requestUri
     *
     * @param requestUri requestUri
     */
    public static void setRequestUri(String requestUri) {
        put(REQUEST_URI, requestUri);
    }

    /**
     * 获取 requestUri
     *
     * @return requestUri
     */
    public static String getRequestUri() {
        return get(REQUEST_URI);
    }

    /**
     * 移除 requestUri
     */
    public static void removeRequestUri() {
        remove(REQUEST_URI);
    }

    /**
     * 设置自定义属性
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * 获取自定义属性
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        return MDC.get(key);
    }

    /**
     * 移除属性
     *
     * @param key 键
     */
    public static void remove(String key) {
        MDC.remove(key);
    }

    /**
     * 清空上下文
     */
    public static void clear() {
        MDC.clear();
    }

    /**
     * 获取所有上下文信息
     *
     * @return 上下文信息
     */
    public static Map<String, String> getContext() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * 设置上下文信息
     *
     * @param contextMap 上下文信息
     */
    public static void setContext(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

}

