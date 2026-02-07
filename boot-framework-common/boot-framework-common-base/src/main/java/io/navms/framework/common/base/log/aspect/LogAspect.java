package io.navms.framework.common.base.log.aspect;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import io.navms.framework.common.base.constant.Constants;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.SimpleTimer;
import io.navms.framework.common.base.utils.ThreadLocalUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 日志切面
 * <p>
 * 拦截 @Log 注解标记的方法，自动记录方法执行日志，包括：方法信息、请求参数、返回值、执行耗时、异常信息等
 *
 * @author navms
 */
@Aspect
@Order(1)
public class LogAspect {

    @Pointcut("@annotation(io.navms.framework.common.base.log.aspect.Log)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        SimpleTimer.start();

        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 方法信息
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String simpleClassName = joinPoint.getTarget().getClass().getSimpleName();

        // 构建日志前缀
        StringBuilder logPrefix = new StringBuilder();
        if (!logAnnotation.module().isEmpty()) {
            logPrefix.append("[").append(logAnnotation.module()).append("] ");
        }
        if (!logAnnotation.bizType().isEmpty()) {
            logPrefix.append("[").append(logAnnotation.bizType()).append("] ");
        }
        if (!logAnnotation.description().isEmpty()) {
            logPrefix.append(logAnnotation.description()).append(" - ");
        }

        Object result = null;
        Throwable exception = null;

        try {
            // 记录方法开始执行
            logMethodStart(joinPoint, logAnnotation, logPrefix.toString(), className, methodName, simpleClassName);

            // 执行目标方法
            result = joinPoint.proceed();

            return result;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            long costTime = SimpleTimer.stop();

            // 记录方法执行结束
            logMethodEnd(logAnnotation, logPrefix.toString(), simpleClassName, methodName,
                    result, exception, costTime);
        }
    }

    private void logMethodStart(ProceedingJoinPoint joinPoint, Log logAnnotation,
                                String logPrefix, String className, String methodName,
                                String simpleClassName) {
        try {
            // 获取上下文信息
            String traceId = ThreadLocalUtils.getString(Constants.TRACE_ID);
            String userId = ThreadLocalUtils.getString(Constants.USER_ID);
            String clientIp = ThreadLocalUtils.getString(Constants.CLIENT_IP);

            StringBuilder logMessage = new StringBuilder();
            logMessage.append(logPrefix)
                    .append("开始执行: ")
                    .append(simpleClassName).append(".").append(methodName).append("()");

            // 添加上下文信息
            List<String> contextInfo = new ArrayList<>();
            if (traceId != null) {
                contextInfo.add("traceId=" + traceId);
            }
            if (userId != null) {
                contextInfo.add("userId=" + userId);
            }
            if (clientIp != null) {
                contextInfo.add("ip=" + clientIp);
            }

            if (!contextInfo.isEmpty()) {
                logMessage.append(" [").append(String.join(", ", contextInfo)).append("]");
            }

            // 记录请求参数
            if (logAnnotation.recordParams()) {
                String params = getMethodParams(joinPoint, logAnnotation);
                if (params != null && !params.isEmpty()) {
                    logMessage.append(", 参数: ").append(params);
                }
            }

            LogUtils.info(logMessage.toString());
        } catch (Exception e) {
            LogUtils.warn("记录方法开始日志失败: {}.{}", className, methodName, e);
        }
    }

    private void logMethodEnd(Log logAnnotation, String logPrefix, String simpleClassName,
                              String methodName, Object result, Throwable exception, long costTime) {
        try {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append(logPrefix)
                    .append("执行完成: ")
                    .append(simpleClassName).append(".").append(methodName).append("()");

            // 记录耗时
            logMessage.append(", 耗时: ").append(costTime).append("ms");

            // 记录返回值
            if (exception == null && logAnnotation.recordResult() && result != null) {
                String resultStr = formatResult(result);
                if (resultStr != null && resultStr.length() <= 500) {
                    logMessage.append(", 返回值: ").append(resultStr);
                } else if (resultStr != null) {
                    logMessage.append(", 返回值: ").append(resultStr, 0, 500).append("...(已截断)");
                }
            }

            // 记录异常信息
            if (exception != null && logAnnotation.recordException()) {
                LogUtils.error(logMessage.toString(), exception);
            } else if (exception != null) {
                LogUtils.error(logMessage.append(", 异常: ").append(exception.getMessage()).toString());
            } else {
                LogUtils.info(logMessage.toString());
            }
        } catch (Exception e) {
            LogUtils.warn("记录方法结束日志失败: {}.{}", simpleClassName, methodName, e);
        }
    }

    /**
     * 获取方法参数信息
     */
    private String getMethodParams(ProceedingJoinPoint joinPoint, Log logAnnotation) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return null;
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();

            // 过滤和处理参数
            Map<String, Object> params = new LinkedHashMap<>();
            Set<Integer> excludeIndexes = new HashSet<>();
            if (logAnnotation.excludeParamIndexes() != null) {
                for (int index : logAnnotation.excludeParamIndexes()) {
                    excludeIndexes.add(index);
                }
            }

            for (int i = 0; i < args.length; i++) {
                // 跳过排除的参数
                if (excludeIndexes.contains(i)) {
                    continue;
                }

                Object arg = args[i];
                String paramName = (parameterNames != null && i < parameterNames.length)
                        ? parameterNames[i] : "arg" + i;
                params.put(paramName, arg);
            }

            if (params.isEmpty()) {
                return null;
            }

            return JSON.toJSONString(params, JSONWriter.Feature.WriteMapNullValue,
                    JSONWriter.Feature.WriteNullStringAsEmpty);
        } catch (Exception e) {
            LogUtils.warn("获取方法参数失败", e);
            return "[参数获取失败: " + e.getMessage() + "]";
        }
    }

    private String formatResult(Object result) {
        try {
            if (result == null) {
                return "null";
            }

            if (result instanceof String || result instanceof Number || result instanceof Boolean) {
                return result.toString();
            }

            return JSON.toJSONString(result, JSONWriter.Feature.WriteMapNullValue,
                    JSONWriter.Feature.WriteNullStringAsEmpty);
        } catch (Exception e) {
            LogUtils.warn("格式化返回值失败", e);
            return "[格式化失败: " + e.getMessage() + "]";
        }
    }

}
