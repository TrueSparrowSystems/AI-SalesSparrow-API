package com.salessparrow.api.dto;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;

public class SalesforceRedirectUrlDtoTest {
  private Validator validator;

  @BeforeEach
  void setUp() {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      validator = factory.getValidator();
  }

  @Test
  void testValidSalesforceRedirectUrlDto() {
      // Arrange
      String redirectUri = "https://redirect.uri";
      String state = "state";
      SalesforceRedirectUrlDto salesforceRedirectUrlDto = new SalesforceRedirectUrlDto(redirectUri, state);

      // Act
      Set<ConstraintViolation<SalesforceRedirectUrlDto>> violations = validator.validate(salesforceRedirectUrlDto);

      // Assert
      Assertions.assertTrue(violations.isEmpty());
  }
}
