package io.navms.framework.common.validation;

import io.navms.framework.common.validation.validator.TelephoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {TelephoneValidator.class}
)
public @interface Telephone {

    String message() default "电话号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
