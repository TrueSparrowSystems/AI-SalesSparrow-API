package com.salessparrow.api.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;


import java.util.Set;

public class UserDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDto() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        UserDto userDto = new UserDto(email, password);

        // Act
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void testUserDtoWithBlankEmail() {
        // Arrange
        String password = "password123";
        UserDto userDto = new UserDto("", password);

        // Act
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<UserDto> violation = violations.iterator().next();
        Assertions.assertEquals("Email is required", violation.getMessage());
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void testUserDtoWithInvalidEmailFormat() {
        // Arrange
        String email = "invalid-email-format";
        String password = "password123";
        UserDto userDto = new UserDto(email, password);

        // Act
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<UserDto> violation = violations.iterator().next();
        Assertions.assertEquals("Invalid email format", violation.getMessage());
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void testUserDtoWithBlankPassword() {
        // Arrange
        String email = "test@example.com";
        UserDto userDto = new UserDto(email, "");

        // Act
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // Assert
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<UserDto> violation = violations.iterator().next();
        Assertions.assertEquals("Password is required", violation.getMessage());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        UserDto userDto = new UserDto(email, password);

        // Act
        String getEmailResult = userDto.getEmail();
        String getPasswordResult = userDto.getPassword();

        // Assert
        Assertions.assertEquals(email, getEmailResult);
        Assertions.assertEquals(password, getPasswordResult);

        // Modify the values using setters
        String newEmail = "newemail@example.com";
        String newPassword = "newpassword456";
        userDto.setEmail(newEmail);
        userDto.setPassword(newPassword);

        // Assert the updated values
        Assertions.assertEquals(newEmail, userDto.getEmail());
        Assertions.assertEquals(newPassword, userDto.getPassword());
    }
    
}
