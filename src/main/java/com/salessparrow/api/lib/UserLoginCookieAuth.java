package com.salessparrow.api.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.lib.globalConstants.UserConstants;
import com.salessparrow.api.repositories.SalesforceUserRepository;

@Component
public class UserLoginCookieAuth {
  private String cookieValue;
  private String userId;
  private String userKind;
  private Integer timestampInCookie;
  private String token;
  private User user;
  private String userLoginCookieValue;
  private String decryptedEncryptionSalt;

  @Autowired
  private LocalCipher localCipher;

  @Autowired
  private CookieHelper cookieHelper;

  @Autowired
  private SalesforceUserRepository salesforceUserRepository;

  /**
   * Validate and set cookie
   *
   * @param cookieValue
   * @param expireTimestamp
   *
   * @return Map<String, Object>
   */
  public Map<String, Object> validateAndSetCookie(String cookieValue) {
    this.cookieValue = cookieValue;

    System.out.println("Validating cookie");
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

  }

  /**
   * Set cookie value parts
   *
   * @throws RuntimeException
   */
  private void setParts() {
    List<String> cookieValueParts = Arrays.asList(cookieValue.split(":"));

    if (cookieValueParts.size() != 5) {
      throw new CustomException(
          new ErrorObject(
              "l_ulca_sp_1",
              "unauthorized_api_request",
              "Invalid cookie value"));
    }

    if (cookieValueParts.get(0).equals(CookieConstants.LATEST_VERSION)) {
      userId = cookieValueParts.get(1);
      userKind = cookieValueParts.get(2);
      timestampInCookie = Integer.parseInt(cookieValueParts.get(3));
      token = cookieValueParts.get(4);
    } else {
      throw new CustomException(
          new ErrorObject(
              "l_ulca_sp_2",
              "unauthorized_api_request",
              "Invalid cookie version"));
    }
  }

  /**
   * Validate cookie timestamp
   *
   * @throws RuntimeException
   */
  private void validateTimestamp() {
    if (timestampInCookie + CookieConstants.USER_LOGIN_COOKIE_EXPIRY_IN_SEC < System.currentTimeMillis() / 1000) {
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

    System.out.println("Fetching and validating user");
    if (userKind.equals(UserConstants.SALESFORCE_USER_KIND)) {
      System.out.println("Fetching and validating salesforce user");
      fetchAndValidateSalesforceUser();
    } else {
      System.out.println("Fetching another user");
      // Todo: Add other user kinds
    }

  }

  private void fetchAndValidateSalesforceUser() {
    User userObj = salesforceUserRepository.getSalesforceUserByExternalUserId(userId);

    if (userObj == null) {
      throw new CustomException(
          new ErrorObject(
              "l_ulca_favu_1",
              "unauthorized_api_request",
              "User not found"));
    }

    user = userObj;
  }

  /**
   * Validate cookie token
   *
   * @throws RuntimeException
   */
  private void validateCookieToken() {
    String encryptionSalt = user.getEncryptionSalt();
    decryptedEncryptionSalt = localCipher.decrypt(CoreConstants.encryptionKey(),
        encryptionSalt);

    String generatedToken = cookieHelper.getCookieToken(user,
        decryptedEncryptionSalt, timestampInCookie);
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
    userLoginCookieValue = cookieHelper.getCookieValue(user, userKind,
        decryptedEncryptionSalt);
  }

}
