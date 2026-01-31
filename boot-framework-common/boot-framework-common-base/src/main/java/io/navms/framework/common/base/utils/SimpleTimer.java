package io.navms.framework.common.base.utils;

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

    public static String stop() {
        try {
            return String.format("%.6f ms", (System.nanoTime() - TIME.get()) / 1_000_000.0);
        } finally {
            TIME.remove();
        }
    }

}
