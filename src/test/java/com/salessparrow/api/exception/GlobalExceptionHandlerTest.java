package com.salessparrow.api.exception;

import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.salessparrow.api.lib.errorLib.ErrorResponseObject;
import com.salessparrow.api.lib.errorLib.ParamErrorConfig;

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
        CustomException ex = new CustomException("b_1", "unauthorized_api_request", "Error test");

        List<ParamErrorConfig> paramErrorConfigList = new ArrayList<>();

        ErrorResponseObject errorResponseObject = new ErrorResponseObject(
            401,
            "Unauthorized API request. Please check your API key.",
            "UNAUTHORIZED",
            "b_1",
            paramErrorConfigList
        );

        System.out.println("Error response object" + errorResponseObject.toString());

        // Act
        ResponseEntity<ErrorResponseObject> result = globalExceptionHandler.handleCustomException(ex);

        // Assert
        Assertions.assertEquals(errorResponseObject.getHttpCode(), result.getStatusCodeValue());
        Assertions.assertEquals(errorResponseObject.getMessage(), result.getBody().getMessage());
        Assertions.assertEquals(errorResponseObject.getCode(), result.getBody().getCode());
        Assertions.assertEquals(errorResponseObject.getInternalErrorIdentifier(), result.getBody().getInternalErrorIdentifier());
        Assertions.assertEquals(errorResponseObject.getErrorData(), result.getBody().getErrorData());
    }

    @Test
    void testHandleRuntimeException() {
        // Arrange
        RuntimeException ex = new RuntimeException("Error test");


        List<ParamErrorConfig> paramErrorConfigList = new ArrayList<>();

        ErrorResponseObject errorResponseObject = new ErrorResponseObject(
            500,
            "Internal Server Error",
            "INTERNAL_SERVER_ERROR",
            "b_1",
            paramErrorConfigList
        );

        // Act
        ResponseEntity<ErrorResponseObject> result = globalExceptionHandler.handleRuntimeException(ex);

        // Assert
        Assertions.assertEquals(errorResponseObject.getHttpCode(), result.getStatusCodeValue());
        Assertions.assertEquals(errorResponseObject.getMessage(), result.getBody().getMessage());
        Assertions.assertEquals(errorResponseObject.getCode(), result.getBody().getCode());
        Assertions.assertEquals(errorResponseObject.getInternalErrorIdentifier(), result.getBody().getInternalErrorIdentifier());
        Assertions.assertEquals(errorResponseObject.getErrorData(), result.getBody().getErrorData());

    }

}
