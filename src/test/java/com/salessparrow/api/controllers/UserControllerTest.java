package com.salessparrow.api.controllers;

import com.salessparrow.api.services.UserLoginService;
import com.salessparrow.api.services.UserLoginService.UserLoginServiceDto;
import com.salessparrow.api.services.UserSignupService;
import com.salessparrow.api.services.UserProfileService;
import com.salessparrow.api.dto.formatter.LoginServiceDto;
import com.salessparrow.api.dto.formatter.SafeUserDto;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.UserDto;
import com.salessparrow.api.lib.CookieHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

  @Mock
  private UserLoginService userLoginService;

  @Mock
  private UserSignupService userSignupService;

  @Mock
  private UserProfileService userProfileService;

  @Mock
  private CookieHelper cookieHelper;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testValidUserSignup() {
    // Input
    UserDto userDto = new UserDto("test1@example.com", "test1234");

    // Mocking service response
    Map<String, Object> userResponse = new HashMap<>();
    userResponse.put("user", userDto);
    userResponse.put("cookie", "auth_cookie");

    // Mocking create user service
    when(userSignupService.createUser(any(UserDto.class))).thenReturn(userResponse);

    // Mocking cookieHelper
    HttpHeaders headers = new HttpHeaders();
    when(cookieHelper.setUserCookie("auth_cookie", headers)).thenReturn(headers);

    // Test
    ResponseEntity<Map<String, Object>> response = userController.signup(userDto);

    // Assertion
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(userDto.getEmail(), ((SafeUserDto) response.getBody().get("user")).getEmail());

    // Verify the interactions
    verify(userSignupService, times(1)).createUser(any(UserDto.class));
    verify(cookieHelper, times(1)).setUserCookie("auth_cookie", headers);
  }

  @Test
  void testUserSignupWithInvalidData() {
    // Input
    UserDto userDto = new UserDto("invalid-email", "test1234");

    // Mocking service response
    when(userSignupService.createUser(any(UserDto.class))).thenThrow(new RuntimeException("Invalid data"));

    // Test
    RuntimeException exception = assertThrows(RuntimeException.class, () -> userController.signup(userDto));

    // Assertion
    assertEquals("Invalid data", exception.getMessage());

    // Verify the interactions
    verify(userSignupService, times(1)).createUser(any(UserDto.class));
    verifyNoInteractions(cookieHelper); // No cookie should be generated for invalid data
  }

  @Test
  void testValidUserLogin() {
    // Input
    UserDto userDto = new UserDto("test1@example.com", "test1234");

    User user = new User();
    user.setEmail(userDto.getEmail());
    user.setPassword(userDto.getPassword());

    UserLoginServiceDto userLoginServiceDto = new UserLoginServiceDto();
    userLoginServiceDto.setUser(user);
    userLoginServiceDto.setCookie("auth_cookie");

    when(userLoginService.authenticateUser(any(UserDto.class))).thenReturn(userLoginServiceDto);

    // Mocking cookieHelper
    HttpHeaders headers = new HttpHeaders();
    when(cookieHelper.setUserCookie("auth_cookie", headers)).thenReturn(headers);

    // Test
    ResponseEntity<LoginServiceDto> response = userController.login(userDto);
    System.out.println("response ------ " + response.getBody());

    // Assertion
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(userDto.getEmail(), ((SafeUserDto) response.getBody().getUser()).getEmail());

    // Verify the interactions
    verify(userLoginService, times(1)).authenticateUser(any(UserDto.class));
    verify(cookieHelper, times(1)).setUserCookie("auth_cookie", headers);
  }

  @Test
  void testUserLoginWithIncorrectPassword() {
    // Input
    UserDto userDto = new UserDto("test1@example.com", "incorrect_password");

    // Mocking service response
    when(userLoginService.authenticateUser(any(UserDto.class))).thenThrow(new RuntimeException("Invalid credentials"));

    // Test
    RuntimeException exception = assertThrows(RuntimeException.class, () -> userController.login(userDto));

    // Assertion
    assertEquals("Invalid credentials", exception.getMessage());

    // Verify the interactions
    verify(userLoginService, times(1)).authenticateUser(any(UserDto.class));
    verifyNoInteractions(cookieHelper); // No cookie should be generated for invalid data
  }

  @Test
  void testUserLogout() {
    // Test
    ResponseEntity<String> response = userController.logout(null);

    // Assertion
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals("User logged out successfully", response.getBody());

    // Verify the interactions
    verifyNoInteractions(cookieHelper);
  }

  @Test
  void testGetUserProfile() {
    // Input
    UserDto userDto = new UserDto("test1@example.com", "test1234");

    // Mocking service response
    Map<String, Object> userProfileResponse = new HashMap<>();
    userProfileResponse.put("email", userDto.getEmail());

    when(userProfileService.getUserProfile(any())).thenReturn(userProfileResponse);

    // Test
    ResponseEntity<Map<String, Object>> response = userController.profile(null, null);

    // Assertion
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(userDto.getEmail(), response.getBody().get("email"));

    // Verify the interactions
    verify(userProfileService, times(1)).getUserProfile(any());
  }
}
