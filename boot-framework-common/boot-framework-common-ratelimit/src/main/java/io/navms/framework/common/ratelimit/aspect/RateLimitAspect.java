package io.navms.framework.common.ratelimit.aspect;

import io.navms.framework.common.base.constant.Constants;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.SecureUtils;
import io.navms.framework.common.base.utils.StringUtils;
import io.navms.framework.common.base.utils.ThreadLocalUtils;
import io.navms.framework.common.ratelimit.annotation.RateLimit;
import io.navms.framework.common.ratelimit.config.RateLimitProperties;
import io.navms.framework.common.ratelimit.exception.RateLimitException;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategy;
import io.navms.framework.common.ratelimit.strategy.RateLimitStrategyFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * 限流切面
 * 基于注解实现方法级别的限流控制
 *
 * @author navms
 */
@Aspect
public class RateLimitAspect {

    private final RateLimitStrategyFactory strategyFactory;
    private final RateLimitProperties properties;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public RateLimitAspect(RateLimitStrategyFactory strategyFactory, RateLimitProperties properties) {
        this.strategyFactory = strategyFactory;
        this.properties = properties;
    }

    @Around("@annotation(io.navms.framework.common.ratelimit.annotation.RateLimit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 检查是否启用限流
        if (!properties.isEnabled()) {
            return point.proceed();
        }

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        RateLimit rateLimit = AnnotationUtils.getAnnotation(method, RateLimit.class);
        if (rateLimit == null) {
            return point.proceed();
        }

        try {
            // 构建限流 key
            String rateLimitKey = buildRateLimitKey(rateLimit, point, method);
            String md5Key = SecureUtils.md5(rateLimitKey);
            String redisKey = properties.getKeyPrefix() + ":" + md5Key;

            if (properties.isLogEnabled()) {
                LogUtils.debug("限流检查 - Key: {}, Algorithm: {}, Count: {}, Time: {}{}",
                        rateLimitKey,
                        rateLimit.algorithm().getDescription(),
                        rateLimit.count(),
                        rateLimit.time(),
                        rateLimit.timeUnit().name());
            }

            // 获取限流策略并执行限流检查
            RateLimitStrategy strategy = strategyFactory.getStrategy(rateLimit.algorithm());
            boolean acquired = strategy.tryAcquire(
                    redisKey,
                    rateLimit.count(),
                    rateLimit.time(),
                    rateLimit.timeUnit()
            );

            if (!acquired) {
                if (properties.isLogEnabled()) {
                    LogUtils.warn("触发限流 - Key: {}, Type: {}, Algorithm: {}",
                            rateLimitKey,
                            rateLimit.type().getDescription(),
                            rateLimit.algorithm().getDescription());
                }
                throw new RateLimitException(rateLimit.message());
            }

            // 获取剩余配额（可选，用于响应头）
            long remaining = strategy.getRemaining(
                    redisKey,
                    rateLimit.count(),
                    rateLimit.time(),
                    rateLimit.timeUnit()
            );

            if (properties.isLogEnabled()) {
                LogUtils.debug("限流通过 - Key: {}, Remaining: {}", rateLimitKey, remaining);
            }

            return point.proceed();
        } catch (RateLimitException e) {
            throw e;
        } catch (Exception e) {
            // 降级处理：如果 Redis 不可用，根据配置决定是否放行
            if (rateLimit.fallback() || properties.isFallback()) {
                LogUtils.error("限流服务异常，已降级放行", e);
                return point.proceed();
            } else {
                LogUtils.error("限流服务异常", e);
                throw new RateLimitException("限流服务异常", e);
            }
        }
    }

    /**
     * 构建限流 key
     *
     * @param rateLimit 限流注解
     * @param point     切点
     * @param method    方法
     * @return 限流 key
     */
    private String buildRateLimitKey(RateLimit rateLimit, ProceedingJoinPoint point, Method method) {
        StringBuilder keyBuilder = new StringBuilder();

        // 基础 key：注解指定的 key 或方法签名
        if (StringUtils.isNotEmpty(rateLimit.key())) {
            keyBuilder.append(rateLimit.key());
        } else {
            keyBuilder.append(method.getDeclaringClass().getName())
                    .append(":")
                    .append(method.getName());
        }

        // 根据限流类型添加额外的 key 部分
        switch (rateLimit.type()) {
            case IP -> keyBuilder.append(":").append(ThreadLocalUtils.getString(Constants.CLIENT_IP));
            case USER -> keyBuilder.append(":").append(ThreadLocalUtils.getLong(Constants.USER_ID));
            case CUSTOM -> {
                String customKey = parseSpEL(rateLimit.customKey(), point, method);
                if (StringUtils.isNotEmpty(customKey)) {
                    keyBuilder.append(":").append(customKey);
                }
            }
            case GLOBAL -> {
                // 全局限流，不添加额外部分
            }
        }

        return keyBuilder.toString();
    }

    /**
     * 解析 SpEL 表达式
     *
     * @param spel   SpEL 表达式
     * @param point  切点
     * @param method 方法
     * @return 解析结果
     */
    private String parseSpEL(String spel, ProceedingJoinPoint point, Method method) {
        if (StringUtils.isBlank(spel)) {
            return "";
        }

        try {
            // 获取方法参数名
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            if (parameterNames == null) {
                return "";
            }

            // 构建 SpEL 上下文
            EvaluationContext context = new StandardEvaluationContext();
            Object[] args = point.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            // 解析表达式
            Expression expression = parser.parseExpression(spel);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            LogUtils.error("SpEL 表达式解析失败: {}", spel, e);
            return "";
        }
    }

}
