package com.salessparrow.api.lib.customAnnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.salessparrow.api.lib.validators.RedirectUriValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RedirectUriValidator.class)
public @interface ValidRedirectUri {
  String message() default "Invalid redirect URI";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}