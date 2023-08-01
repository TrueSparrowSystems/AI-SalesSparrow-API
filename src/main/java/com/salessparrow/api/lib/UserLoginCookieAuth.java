package com.salessparrow.api.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.repositories.UserRepository;

@Component
public class UserLoginCookieAuth {
  private String cookieValue;
  private int expireTimestamp;
  private Long userId;
  private Long timestamp;
  private String token;
  private User user;
  private String userLoginCookieValue;
  private String decryptedEncryptionSalt;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private LocalCipher localCipher;
  @Autowired
  private CoreConstants coreConstants;
  @Autowired
  private CookieHelper cookieHelper;

  /**
   * Validate and set cookie
   * 
   * @param cookieValue
   * @param expireTimestamp
   * 
   * @return Map<String, Object>
   */
  public Map<String, Object> validateAndSetCookie(String cookieValue, int expireTimestamp) {
    this.cookieValue = cookieValue;
    this.expireTimestamp = expireTimestamp;

    validate();
    setParts();
    validateTimestamp();
    fetchAndValidateUser();
    validateCookieToken();
    setCookie();

    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("user", user);
    resultMap.put("userLoginCookieValue", userLoginCookieValue);

    return resultMap;
  }

  /**
   * Validate cookie value and expire timestamp
   * 
   * @throws RuntimeException
   */
  private void validate() {
    if (cookieValue == null || cookieValue.isEmpty()) {

      throw new CustomException(
          new ErrorObject(
              "l_ulca_v_1",
              "unauthorized_api_request",
              "Invalid cookie value"));
    }

    if (expireTimestamp < 0) {
      throw new CustomException(
          new ErrorObject(
              "l_ulca_v_2",
              "unauthorized_api_request",
              "Invalid expire timestamp"));
    }
  }

  /**
   * Set cookie value parts
   * 
   * @throws RuntimeException
   */
  private void setParts() {
    List<String> cookieValueParts = Arrays.asList(cookieValue.split(":"));

    if (cookieValueParts.get(0).equals(CookieConstants.LATEST_VERSION)) {
      if (cookieValueParts.size() != 4) {
        throw new CustomException(
            new ErrorObject(
                "l_ulca_sp_1",
                "unauthorized_api_request",
                "Invalid cookie value"));
      }

      userId = Long.parseLong(cookieValueParts.get(1));
      timestamp = Long.parseLong(cookieValueParts.get(2));
      token = cookieValueParts.get(3);
    }
  }

  /**
   * Validate cookie timestamp
   * 
   * @throws RuntimeException
   */
  private void validateTimestamp() {
    if (timestamp + expireTimestamp < System.currentTimeMillis()) {
      throw new CustomException(
          new ErrorObject(
              "l_ulca_vt_1",
              "unauthorized_api_request",
              "Cookie expired"));

    }
  }

  /**
   * Fetch and validate user
   * 
   * @throws RuntimeException
   */
  private void fetchAndValidateUser() {
    Optional<User> userObj = userRepository.findById(userId);
    user = userObj.orElse(null);

    if (user == null) {
      throw new CustomException(
          new ErrorObject(
              "l_ulca_favu_1",
              "unauthorized_api_request",
              "User not found"));
    }
  }

  /**
   * Validate cookie token
   * 
   * @throws RuntimeException
   */
  private void validateCookieToken() {
    String encryptionSalt = user.getEncryptionSalt();
    decryptedEncryptionSalt = localCipher.decrypt(coreConstants.encryptionKey(), encryptionSalt);

    String generatedToken = cookieHelper.getCookieToken(user, decryptedEncryptionSalt, timestamp);
    if (!generatedToken.equals(token)) {
      throw new CustomException(
        new ErrorObject(
          "l_ulca_vct_2",
          "unauthorized_api_request",
          "Invalid cookie token"));
    }
  }

  /**
   * Set cookie
   * 
   * @throws RuntimeException
   */
  private void setCookie() {
    userLoginCookieValue = cookieHelper.getCookieValue(user, decryptedEncryptionSalt, System.currentTimeMillis());
  }

}
