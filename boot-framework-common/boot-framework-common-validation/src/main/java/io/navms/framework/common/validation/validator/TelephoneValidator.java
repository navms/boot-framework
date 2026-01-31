package io.navms.framework.common.validation.validator;

import io.navms.framework.common.validation.Telephone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 电话号码校验器，用于校验单个字符串是否为合法的电话号码。
 *
 * @author navms
 */
public class TelephoneValidator implements ConstraintValidator<Telephone, String> {

    // 一个相对宽松的电话号码正则表达式，支持：
    // - 可能的国际区号，如 +86, +1
    // - 国内常见格式，如 13812345678, 021-88888888, (021)88888888
    // - 允许中间有空格或横杠作为分隔符
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile(
            "^(\\+\\d{1,3}[- ]?)?\\(?\\d{3,4}\\)?[- ]?\\d{3,4}[- ]?\\d{4}$"
    );

    @Override
    public void initialize(Telephone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return TELEPHONE_PATTERN.matcher(value).matches();
    }

}