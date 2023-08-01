package com.salessparrow.api.interceptors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggerInterceptorTest {

    private LoggerInterceptor loggerInterceptor;

    @BeforeEach
    void setUp() {
        loggerInterceptor = new LoggerInterceptor();
    }

    @Test
    void testPreHandleWithExistingTrackingIdHeader() throws Exception {
        // Arrange
        String trackingId = "existing-tracking-id";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Object handler = new Object();

        Mockito.when(request.getHeader("X-Tracking-Id")).thenReturn(trackingId);

        // Act
        boolean result = loggerInterceptor.preHandle(request, response, handler);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(request, Mockito.never()).setAttribute(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testPreHandleWithMissingTrackingIdHeader() throws Exception {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Object handler = new Object();

        Mockito.when(request.getHeader("X-Tracking-Id")).thenReturn(null);

        // Act
        boolean result = loggerInterceptor.preHandle(request, response, handler);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(request, Mockito.never()).setAttribute(Mockito.anyString(), Mockito.anyString());
    }
}
