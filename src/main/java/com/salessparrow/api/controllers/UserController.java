package com.salessparrow.api.controllers;

import com.salessparrow.api.services.UserSignupService;
import com.salessparrow.api.services.UserLoginService.UserLoginServiceDto;
import com.salessparrow.api.services.UserLoginService;
import com.salessparrow.api.services.UserProfileService;
import com.salessparrow.api.dto.UserDto;
import com.salessparrow.api.dto.formatter.LoginServiceDto;
import com.salessparrow.api.dto.formatter.SafeUserDto;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.globalConstants.CookieConstants;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
  @Autowired
  private UserLoginService UserLoginService;
  @Autowired
  private UserSignupService UserSignupService;
  @Autowired
  private UserProfileService UserProfileService;
  @Autowired
  private CookieHelper cookieHelper;

  Logger logger = LoggerFactory.getLogger(UserController.class);

  @GetMapping("/test")
  public String test() {
    return "User test. Hi";
  }

  @PostMapping("/signup")
  public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserDto userDto) {
    logger.info("User signup request received");

    Map<String, Object> userResponse = UserSignupService.createUser(userDto);

    HttpHeaders headers = new HttpHeaders();
    headers = cookieHelper.setUserCookie(userResponse.get("cookie").toString(), headers);

    SafeUserDto safeUserDto = new ModelMapper().map(userResponse.get("user"), SafeUserDto.class);
    Map<String, Object> signUpResponse = new HashMap<>();
    signUpResponse.put("user", safeUserDto);

    return ResponseEntity.ok().headers(headers).body(signUpResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginServiceDto> login(@RequestBody UserDto userDto) {
    logger.info("User login request received");

    UserLoginServiceDto userLoginServiceDto = UserLoginService.authenticateUser(userDto);

    HttpHeaders headers = new HttpHeaders();
    headers = cookieHelper.setUserCookie(userLoginServiceDto.getCookie(), headers);

    SafeUserDto safeUserDto = new ModelMapper().map(userLoginServiceDto.getUser(), SafeUserDto.class);

    LoginServiceDto loginResponse = new LoginServiceDto();
    loginResponse.setUser(safeUserDto);

    return ResponseEntity.ok().headers(headers).body(loginResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    logger.info("User logout request received");

    // Perform necessary logout actions, such as invalidating the token
    String cookieName = CookieConstants.USER_LOGIN_COOKIE_NAME;
    String cookieValue = "";
    int cookieExpiry = -1;

    // Return the user response with the added cookie in the response headers
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, String.format("%s=%s; Max-Age=%d; Path=/",
        cookieName, cookieValue, cookieExpiry));

    // Return a success response
    return ResponseEntity.ok().headers(headers).body("User logged out successfully");
  }

  @GetMapping("/profile")
  public ResponseEntity<Map<String, Object>> profile(HttpServletRequest request, HttpServletResponse response) {
    logger.info("User profile request received");

    Map<String, Object> profileResponse = UserProfileService.getUserProfile(request);

    return ResponseEntity.ok().body(profileResponse);
  }
}
