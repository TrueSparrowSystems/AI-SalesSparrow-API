package com.salessparrow.api.interceptors;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.UserLoginCookieAuth;
import com.salessparrow.api.lib.globalConstants.CookieConstants;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthInterceptorTest {
    
    @InjectMocks
    private UserAuthInterceptor userAuthInterceptor;

    @Mock
    private UserLoginCookieAuth userLoginCookieAuth;

    @Mock
    private CookieHelper cookieHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPreHandleWithValidCookie() throws Exception {
        // Arrange
        String cookieValue = "valid-cookie-value";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Object handler = new Object();
        String userLoginCookieValue = "new-cookie-value";

        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue)});
        Mockito.when(userLoginCookieAuth.validateAndSetCookie(cookieValue, CookieConstants.USER_LOGIN_COOKIE_EXPIRY_IN_MS))
                .thenReturn(Map.of("user", new User(), "userLoginCookieValue", userLoginCookieValue));

        HttpHeaders headers = new HttpHeaders();
        Mockito.when(cookieHelper.setCookieInHeaders(
                CookieConstants.USER_LOGIN_COOKIE_NAME,
                userLoginCookieValue,
                CookieConstants.USER_LOGIN_COOKIE_EXPIRY_IN_MS,
                headers))
                .thenReturn(headers);

        // Act
        boolean result = userAuthInterceptor.preHandle(request, response, handler);

        // Assert
        Assertions.assertTrue(result);
    }
}
