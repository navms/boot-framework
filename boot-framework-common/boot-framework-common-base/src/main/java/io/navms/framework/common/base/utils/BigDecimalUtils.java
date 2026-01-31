package io.navms.framework.common.base.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;

/**
 * BigDecimal 工具类
 *
 * @author navms
 */
public abstract class BigDecimalUtils {

    /**
     * 默认保留小数位数
     */
    public static final int DEFAULT_SCALE = 2;

    /**
     * 默认舍入模式（四舍五入）
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    // ========================= 转换方法 =========================

    /**
     * 将对象安全转换为 BigDecimal
     *
     * @param obj 待转换对象（支持 String、Number 等）
     * @return 转换后的 BigDecimal，转换失败或 null 时返回 ZERO
     */
    public static BigDecimal toBigDecimal(Object obj) {
        switch (obj) {
            case BigDecimal bigDecimal -> {
                return bigDecimal;
            }
            case Number ignored -> {
                return new BigDecimal(obj.toString());
            }
            case String s -> {
                return new BigDecimal(s);
            }
            case null, default -> {
                return BigDecimal.ZERO;
            }
        }
    }

    // ========================= 四则运算 =========================

    /**
     * 加法运算
     *
     * @param a 被加数
     * @param b 加数
     * @return a + b 的结果
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).add(toBigDecimal(b));
    }

    /**
     * 减法运算
     *
     * @param a 被减数
     * @param b 减数
     * @return a - b 的结果
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).subtract(toBigDecimal(b));
    }

    /**
     * 乘法运算
     *
     * @param a 乘数
     * @param b 乘数
     * @return a * b 的结果
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).multiply(toBigDecimal(b));
    }

    /**
     * 除法运算（使用默认精度和舍入模式）
     *
     * @param a 被除数
     * @param b 除数（不可为零）
     * @return a / b 的结果（默认保留 2 位小数，四舍五入）
     * @throws ArithmeticException 除数为零时抛出
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return divide(a, b, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 除法运算（自定义精度和舍入模式）
     *
     * @param a            被除数
     * @param b            除数（不可为零）
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return a / b 的结果
     * @throws ArithmeticException 除数为零时抛出
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale, RoundingMode roundingMode) {
        BigDecimal aSafe = toBigDecimal(a);
        BigDecimal bSafe = toBigDecimal(b);

        Requires.requireTrue(bSafe.compareTo(BigDecimal.ZERO) != 0, () -> new ArithmeticException("除数不能为零"));
        Requires.requireTrue(scale >= 0, "保留小数位数不能为负数");
        Requires.requireNotNull(roundingMode, "舍入模式不能为 null");

        return aSafe.divide(bSafe, scale, roundingMode);
    }

    // ========================= 比较方法 =========================

    /**
     * 判断 a 是否大于 b
     *
     * @param a 比较数
     * @param b 被比较数
     * @return a > b 则返回 true
     */
    public static boolean gt(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) > 0;
    }

    /**
     * 判断 a 是否小于 b
     *
     * @param a 比较数
     * @param b 被比较数
     * @return a < b 则返回 true
     */
    public static boolean lt(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) < 0;
    }

    /**
     * 判断 a 是否等于 b
     *
     * @param a 比较数
     * @param b 被比较数
     * @return a == b 则返回 true
     */
    public static boolean eq(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) == 0;
    }

    /**
     * 判断 a 是否大于等于 b
     *
     * @param a 比较数
     * @param b 被比较数
     * @return a >= b 则返回 true
     */
    public static boolean ge(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) >= 0;
    }

    /**
     * 判断 a 是否小于等于 b
     *
     * @param a 比较数
     * @param b 被比较数
     * @return a <= b 则返回 true
     */
    public static boolean le(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) <= 0;
    }

    /**
     * 判断数值是否为零
     *
     * @param num 待判断数值
     * @return 为零则返回 true
     */
    public static boolean isZero(BigDecimal num) {
        return toBigDecimal(num).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断数值是否为正数（大于零）
     *
     * @param num 待判断数值
     * @return 为正数则返回 true
     */
    public static boolean isPositive(BigDecimal num) {
        return gt(num, BigDecimal.ZERO);
    }

    /**
     * 判断数值是否为负数（小于零）
     *
     * @param num 待判断数值
     * @return 为负数则返回 true
     */
    public static boolean isNegative(BigDecimal num) {
        return lt(num, BigDecimal.ZERO);
    }

    // ========================= 数值处理 =========================

    /**
     * 取相反数
     *
     * @param num 待处理数值
     * @return 相反数
     */
    public static BigDecimal negate(BigDecimal num) {
        return toBigDecimal(num).negate();
    }

    /**
     * 取绝对值
     *
     * @param num 待处理数值
     * @return 绝对值
     */
    public static BigDecimal abs(BigDecimal num) {
        return toBigDecimal(num).abs();
    }

    /**
     * 设置小数位数（四舍五入）
     *
     * @param num   待处理数值
     * @param scale 保留小数位数
     * @return 处理后的数值
     */
    public static BigDecimal setScale(BigDecimal num, int scale) {
        return setScale(num, scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 设置小数位数（自定义舍入模式）
     *
     * @param num          待处理数值
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return 处理后的数值
     */
    public static BigDecimal setScale(BigDecimal num, int scale, RoundingMode roundingMode) {
        Requires.requireTrue(scale >= 0, "保留小数位数不能为负数");
        Requires.requireNotNull(roundingMode, "舍入模式不能为 null");
        return toBigDecimal(num).setScale(scale, roundingMode);
    }

    // ========================= 格式化 =========================

    /**
     * 格式化 BigDecimal 为指定格式的字符串
     *
     * @param num     待格式化数值
     * @param pattern 格式模板（如 "#,##0.00" 表示千分位分隔，保留两位小数）
     * @return 格式化后的字符串
     */
    public static String format(BigDecimal num, String pattern) {
        Requires.requireNotEmpty(pattern, "格式模板不能为空");

        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(toBigDecimal(num));
    }

    /**
     * 格式化 BigDecimal 为默认格式（保留两位小数）
     *
     * @param num 待格式化数值
     * @return 格式化后的字符串（如 "1,234.56"）
     */
    public static String formatDefault(BigDecimal num) {
        return format(num, "#,##0.00");
    }

    // ========================= 集合运算 =========================

    /**
     * 计算集合中所有数值的总和
     *
     * @param nums 数值集合（元素可为 null，自动视为 0）
     * @return 总和
     */
    public static BigDecimal sum(Collection<BigDecimal> nums) {
        if (CollectionUtils.isEmpty(nums)) {
            return BigDecimal.ZERO;
        }
        return nums.stream().map(BigDecimalUtils::toBigDecimal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * 计算集合中所有数值的乘积
     *
     * @param nums 数值集合（元素可为 null，自动视为 0；若包含 0 则结果为 0）
     * @return 乘积
     */
    public static BigDecimal product(Collection<BigDecimal> nums) {
        if (CollectionUtils.isEmpty(nums)) {
            return BigDecimal.ZERO;
        }
        boolean hasZero = nums.stream().map(BigDecimalUtils::toBigDecimal).anyMatch(BigDecimalUtils::isZero);
        if (hasZero) {
            return BigDecimal.ZERO;
        }
        return nums.stream().map(BigDecimalUtils::toBigDecimal).reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    // ========================= 最值 =========================

    /**
     * 获取两个数中的最大值
     *
     * @param a 数值1
     * @param b 数值2
     * @return 最大值
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).max(toBigDecimal(b));
    }

    /**
     * 获取两个数中的最小值
     *
     * @param a 数值1
     * @param b 数值2
     * @return 最小值
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return toBigDecimal(a).min(toBigDecimal(b));
    }

    /**
     * 获取集合中的最大值
     *
     * @param nums 数值集合（元素可为 null，自动视为 0）
     * @return 最大值（集合为空时返回 0）
     */
    public static BigDecimal max(Collection<BigDecimal> nums) {
        if (CollectionUtils.isEmpty(nums)) {
            return BigDecimal.ZERO;
        }
        BigDecimal max = BigDecimal.ZERO;
        for (BigDecimal num : nums) {
            BigDecimal current = toBigDecimal(num);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }

    /**
     * 获取集合中的最小值
     *
     * @param nums 数值集合（元素可为 null，自动视为 0）
     * @return 最小值（集合为空时返回 0）
     */
    public static BigDecimal min(Collection<BigDecimal> nums) {
        if (CollectionUtils.isEmpty(nums)) {
            return BigDecimal.ZERO;
        }
        BigDecimal min = null;
        for (BigDecimal num : nums) {
            BigDecimal current = toBigDecimal(num);
            if (min == null || current.compareTo(min) < 0) {
                min = current;
            }
        }
        return min == null ? BigDecimal.ZERO : min;
    }

}