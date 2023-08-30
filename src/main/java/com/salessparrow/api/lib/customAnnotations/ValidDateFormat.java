package com.salessparrow.api.lib.customAnnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.salessparrow.api.lib.validators.DateFormatValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
public @interface ValidDateFormat {

	String message() default "Invalid Date Format";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}