package com.salessparrow.api.lib.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.salessparrow.api.lib.customAnnotations.ValidRedirectUri;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.salessparrow.api.config.CoreConstants;

public class RedirectUriValidator implements ConstraintValidator<ValidRedirectUri, String> {

  private List<String> ALLOWED_URIS = new ArrayList<>(
      Arrays.asList(CoreConstants.getWhitelistedRedirectUris()));

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && ALLOWED_URIS.contains(value);
  }
}