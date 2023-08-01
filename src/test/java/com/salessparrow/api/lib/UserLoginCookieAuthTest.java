package com.salessparrow.api.lib;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.repositories.UserRepository;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.exception.CustomException;

public class UserLoginCookieAuthTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private LocalCipher localCipher;

  @Mock
  private CoreConstants coreConstants;

  @Mock
  private CookieHelper cookieHelper;

  @InjectMocks
  private UserLoginCookieAuth userLoginCookieAuth;

  @BeforeEach
  void setup() {
      MockitoAnnotations.openMocks(this);
  }

  @Test
  void testValidateAndSetCookie_ValidCookieValue_ValidUser() {
    // Arrange
    Long currentTime = System.currentTimeMillis();
    String currentTimeString = currentTime.toString();

    String cookieValue = CookieConstants.LATEST_VERSION + ":1:" + currentTimeString + ":abcde";
    int expireTimestamp = 3600000; // 1 hour

    User user = new User();
    user.setId(1L);
    user.setEncryptionSalt("salt123");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(localCipher.decrypt("salt123", user.getEncryptionSalt())).thenReturn("decrypted_salt"); // Correctly set up the mock
    when(cookieHelper.getCookieToken(any(), any(), any())).thenReturn("abcde");
    when(cookieHelper.getCookieValue(any(), any(), any())).thenReturn("xyz");

    // Act
    Map<String, Object> resultMap = userLoginCookieAuth.validateAndSetCookie(cookieValue, expireTimestamp);

    // Assert
    Assertions.assertNotNull(resultMap);
    Assertions.assertEquals(user, resultMap.get("user"));
    Assertions.assertNotNull(resultMap.get("userLoginCookieValue"));
  }

  @Test
  void testValidateAndSetCookie_InvalidCookieValue_ThrowsCustomException() {
    // Arrange
    String cookieValue = "";
    int expireTimestamp = 3600000; // 1 hour

    // Act & Assert
    Assertions.assertThrows(CustomException.class,
          () -> userLoginCookieAuth.validateAndSetCookie(cookieValue, expireTimestamp));
  }

  @Test
  void testValidateAndSetCookie_UserNotFound_ThrowsCustomException() {
    // Arrange
    String cookieValue = CookieConstants.LATEST_VERSION + ":9999:1627239300000:abcde"; // User ID 9999 does not exist
    int expireTimestamp = 3600000; // 1 hour

    when(userRepository.findById(9999L)).thenReturn(Optional.empty());

    // Act & Assert
    Assertions.assertThrows(CustomException.class,
          () -> userLoginCookieAuth.validateAndSetCookie(cookieValue, expireTimestamp));
  }
}
