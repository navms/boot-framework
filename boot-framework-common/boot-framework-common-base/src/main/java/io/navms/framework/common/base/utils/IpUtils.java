package io.navms.framework.common.base.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取客户端 IP 地址工具类
 *
 * @author navms
 */
public abstract class IpUtils {

    /**
     * 获取客户端 IP 地址
     *
     * @param request HttpServletRequest
     * @return IP 地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        // 优先从X-Forwarded-For头部获取，适用于经过代理的情况
        String ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // 多个IP时，第一个IP为客户端真实IP
            return ip.split(",")[0].trim();
        }

        // 检查X-Real-IP头部
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 检查Proxy-Client-IP头部
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 检查WL-Proxy-Client-IP头部（WebLogic）
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 检查HTTP_CLIENT_IP头部
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }

        // 检查HTTP_X_FORWARDED_FOR头部
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip;
        }

        // 最后使用getRemoteAddr()
        ip = request.getRemoteAddr();
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            // 如果是本地环回地址，尝试获取本机真实IP
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                // 忽略异常，使用默认IP
            }
        }
        return ip;
    }

    /**
     * 验证 IP 地址是否有效
     *
     * @param ip IP 地址
     * @return 是否有效
     */
    private static boolean isValidIp(String ip) {
        return ip != null
                && !ip.isEmpty()
                && !"unknown".equalsIgnoreCase(ip)
                && !"0:0:0:0:0:0:0:1".equals(ip)
                && !ip.contains("127.0.0.1");
    }

    /**
     * 将IP地址转换为长整型（用于存储或比较）
     *
     * @param ip IPv4地址
     * @return 长整型数值
     */
    public static long ipToLong(String ip) {
        String[] segments = ip.split("\\.");
        if (segments.length != 4) {
            return 0;
        }
        return (Long.parseLong(segments[0]) << 24) + (Long.parseLong(segments[1]) << 16) +
                (Long.parseLong(segments[2]) << 8) + Long.parseLong(segments[3]);
    }

    /**
     * 将长整型转换为 IP 地址
     *
     * @param ipLong 长整型数值
     * @return IPv4地址
     */
    public static String longToIp(long ipLong) {
        return ((ipLong >> 24) & 0xFF) + "." + ((ipLong >> 16) & 0xFF) +
                "." + ((ipLong >> 8) & 0xFF) + "." + (ipLong & 0xFF);
    }

}