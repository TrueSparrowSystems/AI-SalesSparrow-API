package com.salessparrow.api.lib.customAnnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.salessparrow.api.lib.validators.DatetimeFormatValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatetimeFormatValidator.class)
public @interface ValidDatetimeFormat {

	String message() default "Invalid Datetime Format";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}