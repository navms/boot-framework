package io.navms.framework.common.base.utils;

import java.util.concurrent.TimeUnit;

/**
 * 简单计时器
 *
 * @author navms
 */
public abstract class SimpleTimer {

    private static final ThreadLocal<Long> TIME = ThreadLocal.withInitial(() -> 0L);

    public static void start() {
        TIME.set(System.nanoTime());
    }

    public static long stop() {
        return stop(TimeUnit.MILLISECONDS);
    }

    public static long stop(TimeUnit unit) {
        try {
            return unit.convert(System.nanoTime() - TIME.get(), TimeUnit.NANOSECONDS);
        } finally {
            TIME.remove();
        }
    }

}
