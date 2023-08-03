// package com.salessparrow.api.lib;

// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.HttpHeaders;

// import com.salessparrow.api.config.CoreConstants;
// import com.salessparrow.api.domain.User;
// import com.salessparrow.api.lib.globalConstants.CookieConstants;

// public class CookieHelperTest {

// @Mock
// private LocalCipher localCipher;

// @Mock
// private CoreConstants coreConstants;

// @InjectMocks
// private CookieHelper cookieHelper;

// @BeforeEach
// void setup() {
// MockitoAnnotations.openMocks(this);
// }

// @Test
// void testGetCookieValue_ValidUser() {
// // Arrange
// Long userId = 1L;
// Long timestamp = System.currentTimeMillis();
// String cookieToken = "cookieToken123";
// String decryptedSaltValue = "decryptedSaltValue123";

// User user = new User();
// user.setId(userId);
// user.setCookieToken("encryptedCookieToken");

// when(localCipher.decrypt(anyString(),
// anyString())).thenReturn(decryptedSaltValue);
// when(coreConstants.apiCookieSecret()).thenReturn("apiSecret");
// when(localCipher.encrypt(anyString(), anyString())).thenReturn(cookieToken);

// // Act
// String cookieValue = cookieHelper.getCookieValue(user, decryptedSaltValue,
// timestamp);
// System.out.println("cookieValue: " + cookieValue);

// // Assert
// String expectedCookieValue = CookieConstants.LATEST_VERSION + ':' + userId +
// ':' + timestamp + ':' + cookieToken;
// System.out.println("expectedCookieValue: " + expectedCookieValue);
// assertEquals(expectedCookieValue, cookieValue);
// }

// @Test
// void testSetUserCookie_ValidCookieValue() {
// // Arrange
// String cookieValue = "cookieValue123";
// HttpHeaders headers = new HttpHeaders();

// // Act
// HttpHeaders resultHeaders = cookieHelper.setUserCookie(cookieValue, headers);

// // Assert
// assertTrue(resultHeaders.containsKey(HttpHeaders.SET_COOKIE));
// String setCookieHeader = resultHeaders.getFirst(HttpHeaders.SET_COOKIE);
// assertTrue(setCookieHeader.contains(cookieValue));
// }

// @Test
// void testGetCookieToken_ValidUser() throws Exception {
// // Arrange
// Long userId = 1L;
// Long timestamp = System.currentTimeMillis();
// String decryptedSalt = "decryptedSalt123";
// String encryptedCookieToken = "encryptedCookieToken123";

// User user = new User();
// user.setId(userId);
// user.setCookieToken("cookieToken123");

// when(localCipher.decrypt(anyString(),
// anyString())).thenReturn(decryptedSalt);
// when(coreConstants.apiCookieSecret()).thenReturn("apiSecret");
// when(localCipher.encrypt(anyString(),
// anyString())).thenReturn(encryptedCookieToken);

// // Act
// String cookieToken = cookieHelper.getCookieToken(user, decryptedSalt,
// timestamp);

// // Assert
// assertEquals(encryptedCookieToken, cookieToken);
// }

// @Test
// void testSetCookieInHeaders_ValidCookieValue() {
// // Arrange
// String cookieName = "testCookie";
// String cookieValue = "testValue";
// int cookieExpiryInMs = 3600000; // 1 hour
// HttpHeaders headers = new HttpHeaders();

// // Act
// HttpHeaders resultHeaders = cookieHelper.setCookieInHeaders(cookieName,
// cookieValue, cookieExpiryInMs, headers);

// // Assert
// assertTrue(resultHeaders.containsKey(HttpHeaders.SET_COOKIE));
// String setCookieHeader = resultHeaders.getFirst(HttpHeaders.SET_COOKIE);
// assertTrue(setCookieHeader.contains(cookieName + "=" + cookieValue));
// assertTrue(setCookieHeader.contains("Max-Age=3600")); // Expiry in seconds
// assertTrue(setCookieHeader.contains("Path=/"));
// }
// }
