package com.salessparrow.api.exception;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.salessparrow.api.lib.ErrorObject;

public class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ErrorResponse er;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleCustomException() {
        // Arrange
        CustomException ex = new CustomException(
                new ErrorObject("s_uss_cu_1", "user_already_exists", "User already exists")
        );

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("http_code", "403");
        errorResponse.put("code", "USER_ALREADY_EXISTS");
        errorResponse.put("message", "User with the provided email already exists.");

        when(er.getErrorResponse(any(), any(), any())).thenReturn(errorResponse);

        // Act
        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleCustomException(ex);

        // Assert
        Assertions.assertEquals(403, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

    @Test
    void testHandleRuntimeException() {
        // Arrange
        RuntimeException ex = new RuntimeException("Test runtime exception");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("http_code", "500");
        errorResponse.put("code", "something_went_wrong");
        errorResponse.put("message", "Test runtime exception");
        errorResponse.put("internal_error_identifier", "b_1");

        when(er.getErrorResponse(any(), any(), any())).thenReturn(errorResponse);

        // Act
        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleRuntimeException(ex);

        // Assert
        Assertions.assertEquals(500, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

    @Test
    void testHandleValidationException() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("testObject", "testField", "Test validation error");
        when(ex.getBindingResult()).thenReturn(mock(BindingResult.class));
        when(ex.getBindingResult().getFieldErrors()).thenReturn(List.of(fieldError));

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("testField", "Test validation error");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("http_code", "400");
        errorResponse.put("code", "invalid_params");
        errorResponse.put("message", errorMap.toString());
        errorResponse.put("internal_error_identifier", "b_2");

        when(er.getErrorResponse(any(), any(), any())).thenReturn(errorResponse);

        // Act
        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleValidationException(ex);

        // Assert
        Assertions.assertEquals(400, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }
}
