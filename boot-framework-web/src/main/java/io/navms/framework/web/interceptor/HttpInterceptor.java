package io.navms.framework.web.interceptor;

import com.alibaba.fastjson2.JSON;
import io.navms.framework.common.base.constant.Constants;
import io.navms.framework.common.base.log.LogContext;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.*;
import io.navms.framework.web.servlet.RepeatableHttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Http 拦截器
 *
 * @author navms
 */
public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            SimpleTimer.start();

            String tid = LogContext.generateTraceId();
            String clientIp = IpUtils.getClientIp(request);
            LogContext.setClientIp(clientIp);

            String parameter = null;
            if (request instanceof RepeatableHttpServletRequestWrapper requestWrapper) {
                Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
                byte[] cachedBytes = requestWrapper.getCachedBytes();

                String encoding = requestWrapper.getRequest().getCharacterEncoding();
                if (StringUtils.isEmpty(encoding)) {
                    encoding = Charset.defaultCharset().name();
                }
                String body = new String(cachedBytes, encoding);
                if (CollectionUtils.isEmpty(parameterMap)) {
                    parameter = body;
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("body", body);
                    map.put("query", parameterMap);
                    parameter = JSON.toJSONString(map);
                }
            }

            // 设置信息到线程上下文
            ThreadLocalUtils.set(Constants.TRACE_ID, tid);
            ThreadLocalUtils.set(Constants.CLIENT_IP, clientIp);
            ThreadLocalUtils.set(Constants.ARGUMENTS, parameter);
            ThreadLocalUtils.set(Constants.REQUEST_URI, request.getRequestURI());
            ThreadLocalUtils.set(Constants.USER_AGENT, request.getHeader(Constants.USER_AGENT));
            // 设置 tradeId 到响应头
            response.addHeader(Constants.TRACE_ID, tid);

            LogUtils.info("请求开始 >>> Method: {}, URI: {}, 参数: {}", request.getMethod(), request.getRequestURI(), parameter);
        } catch (Exception e) {
            LogUtils.error("Http 拦截器 preHandle 异常", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LogUtils.info("请求结束 <<< Method: {}, URI: {}, Status: {}, Cost: {}", request.getMethod(),
                request.getRequestURI(), response.getStatus(), SimpleTimer.stop());

        LogContext.clear();
        ThreadLocalUtils.clear();
    }

}

