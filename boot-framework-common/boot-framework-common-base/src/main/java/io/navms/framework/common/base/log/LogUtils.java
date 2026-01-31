package io.navms.framework.common.base.log;

import io.navms.framework.common.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 * @author navms
 */
public class LogUtils {

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(
            StackWalker.Option.RETAIN_CLASS_REFERENCE);

    // ========================== trace ==========================

    public static void trace(String format, Object... args) {
        trace(getCallerClass(), format, args);
    }

    public static void trace(String format, Throwable t, Object... args) {
        trace(getCallerClass(), format, t, args);
    }

    public static void trace(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isTraceEnabled()) {
            logger.trace(format(format, args));
        }
    }

    public static void trace(Class<?> clazz, String format, Throwable t, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isTraceEnabled()) {
            logger.trace(format(format, args), t);
        }
    }

    // ========================== debug ==========================

    public static void debug(String format, Object... args) {
        debug(getCallerClass(), format, args);
    }

    public static void debug(String format, Throwable t, Object... args) {
        debug(getCallerClass(), format, t, args);
    }

    public static void debug(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(format(format, args));
        }
    }

    public static void debug(Class<?> clazz, String format, Throwable t, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(format(format, args), t);
        }
    }

    // ========================== info ==========================

    public static void info(String format, Object... args) {
        info(getCallerClass(), format, args);
    }

    public static void info(String format, Throwable t, Object... args) {
        info(getCallerClass(), format, t, args);
    }

    public static void info(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(format(format, args));
        }
    }

    public static void info(Class<?> clazz, String format, Throwable t, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(format(format, args), t);
        }
    }

    // ========================== warn ==========================

    public static void warn(String format, Object... args) {
        warn(getCallerClass(), format, args);
    }

    public static void warn(String format, Throwable t, Object... args) {
        warn(getCallerClass(), format, t, args);
    }

    public static void warn(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(format(format, args));
        }
    }

    public static void warn(Class<?> clazz, String format, Throwable t, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(format(format, args), t);
        }
    }

    // ========================== error ==========================

    public static void error(String format, Object... args) {
        error(getCallerClass(), format, args);
    }

    public static void error(String format, Throwable t, Object... args) {
        error(getCallerClass(), format, t, args);
    }

    public static void error(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(format(format, args));
        }
    }

    public static void error(Class<?> clazz, String format, Throwable t, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(format(format, args), t);
        }
    }

    // ========================== 公共工具方法 ==========================

    private static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    private static Class<?> getCallerClass() {
        Class<?> walk = STACK_WALKER.walk(s -> s.skip(2).findFirst()
                .map(StackWalker.StackFrame::getDeclaringClass).orElse(null));
        return walk == null ? LogUtils.class : walk;
    }

    public static String format(String template, Object... args) {
        return StringUtils.format(template, args);
    }

}
