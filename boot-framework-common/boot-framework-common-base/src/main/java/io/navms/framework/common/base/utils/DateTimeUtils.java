package io.navms.framework.common.base.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 日期时间工具类
 *
 * @author navms
 */
public abstract class DateTimeUtils extends LocalDateTimeUtil {

    /**
     * 将时间转换为 Duration
     *
     * @param time     时间数值
     * @param timeUnit 时间单位
     * @return Duration 对象
     */
    public static Duration ofDuration(long time, TimeUnit timeUnit) {
        return switch (timeUnit) {
            case NANOSECONDS -> Duration.ofNanos(time);
            case MICROSECONDS -> Duration.ofNanos(TimeUnit.MICROSECONDS.toNanos(time));
            case MILLISECONDS -> Duration.ofMillis(time);
            case SECONDS -> Duration.ofSeconds(time);
            case MINUTES -> Duration.ofMinutes(time);
            case HOURS -> Duration.ofHours(time);
            case DAYS -> Duration.ofDays(time);
        };
    }

}