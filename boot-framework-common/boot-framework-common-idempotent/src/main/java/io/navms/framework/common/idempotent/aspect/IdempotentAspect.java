package io.navms.framework.common.idempotent.aspect;

import io.navms.framework.cache.utils.CacheUtil;
import io.navms.framework.cache.utils.LockUtil;
import io.navms.framework.common.base.constant.Constants;
import io.navms.framework.common.base.domain.Result;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.utils.*;
import io.navms.framework.common.idempotent.annotation.Idempotent;
import io.navms.framework.common.idempotent.enums.IdempotentScene;
import io.navms.framework.common.idempotent.exception.IdempotentCode;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 幂等切面
 *
 * @author navms
 */
@Aspect
public class IdempotentAspect {

    private static final String LOCK_KEY_PREFIX = "idempotent:lock:";
    private static final String IDEMPOTENT_KEY_PREFIX = "idempotent:";

    @Around("@annotation(io.navms.framework.common.idempotent.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        Idempotent idempotent = AnnotationUtils.getAnnotation(method, Idempotent.class);
        if (idempotent == null) {
            return point.proceed();
        }

        String bizKey = buildIdempotentKey(idempotent);
        String md5Hex = SecureUtils.md5(bizKey);
        String lockKey = LOCK_KEY_PREFIX + md5Hex;
        String idempotentKey = IDEMPOTENT_KEY_PREFIX + md5Hex;
        LogUtils.debug("bizKey: {}, lockKey: {}, idempotentKey: {}", bizKey, lockKey, idempotentKey);

        boolean locked = LockUtil.tryLock(lockKey);
        if (locked) {
            try {
                if (CacheUtil.setIfAbsent(idempotentKey, idempotentKey,
                        DateTimeUtils.ofDuration(idempotent.period(), TimeUnit.MILLISECONDS))) {
                    return point.proceed();
                } else {
                    return Result.fail(IdempotentCode.DO_NOT_REPEAT);
                }
            } finally {
                LockUtil.unlock(lockKey);
            }
        }
        return Result.fail(IdempotentCode.DO_NOT_REPEAT);
    }

    private static String buildIdempotentKey(Idempotent idempotent) {
        if (idempotent.scene() == IdempotentScene.WEB) {
            StringBuilder keyBuilder = new StringBuilder();
            if (idempotent.perClient()) {
                keyBuilder.append(ThreadLocalUtils.getString(Constants.CLIENT_IP));
            }
            keyBuilder.append(ThreadLocalUtils.getString(Constants.REQUEST_URI));
            keyBuilder.append(ThreadLocalUtils.getString(Constants.ARGUMENTS));
            keyBuilder.append(ThreadLocalUtils.getString(Constants.USER_AGENT));
            HttpServletRequest request = ((ServletRequestAttributes)
                    Requires.requireNotNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String[] headers = idempotent.headers();
            if (ArrayUtils.isNotEmpty(headers)) {
                for (String header : headers) {
                    String headerValue = request.getHeader(header);
                    if (StringUtils.isNotEmpty(headerValue)) {
                        keyBuilder.append(headerValue);
                    }
                }
            }
            return keyBuilder.toString();
        }
        return null;
    }

}
