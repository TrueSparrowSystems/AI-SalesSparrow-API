package com.salessparrow.api.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CookieConstants;

import jakarta.servlet.http.Cookie;

@Component
public class CookieHelper {

  @Autowired
  private LocalCipher localCipher;
  @Autowired
  private CoreConstants coreConstants;

  public String getCookieValue(User user, String userKind, String decryptedSalt, Long timestamp) {

    String cookieToken = getCookieToken(user, decryptedSalt, timestamp);

    if (user.getId() == null) {
      throw new CustomException(
          new ErrorObject(
              "l_ch_gcv_1",
              "internal_server_error",
              "User is null"));
    }

    return CookieConstants.LATEST_VERSION + ':' + user.getId() + ':' + userKind + ':' + timestamp + ':' + cookieToken;
  }

  public String getCookieToken(User user, String decryptedSalt, Long timestamp) {

    String decryptedCookieToken = localCipher.decrypt(decryptedSalt, user.getCookieToken());
    String strSecret = coreConstants.apiCookieSecret();
    String stringToSign = user.getId() + ':' + timestamp + ':' + strSecret + ':'
        + decryptedCookieToken.substring(0, 16);
    String salt = user.getId() + ':' + decryptedCookieToken.substring(decryptedCookieToken.length() - 16) + ':'
        + strSecret + ':' + timestamp;
    String encryptedCookieToken = localCipher.encrypt(salt, stringToSign);

    return encryptedCookieToken;
  }

  public HttpHeaders setCookieInHeaders(String cookieName, String cookieValue, int cookieExpiryInMs,
      HttpHeaders headers) {
    Integer cookieExpiryInSecond = cookieExpiryInMs / 1000;

    Cookie cookie = new Cookie(cookieName, cookieValue);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(cookieExpiryInSecond);

    headers.add(HttpHeaders.SET_COOKIE, String.format("%s=%s; Max-Age=%d; Path=/",
        cookieName, cookieValue, cookieExpiryInSecond));

    return headers;
  }

  public HttpHeaders setUserCookie(String cookieValue, HttpHeaders headers) {
    String cookieName = CookieConstants.USER_LOGIN_COOKIE_NAME;
    int cookieExpiry = CookieConstants.USER_LOGIN_COOKIE_EXPIRY_IN_MS;

    headers = setCookieInHeaders(cookieName, cookieValue, cookieExpiry, headers);

    return headers;
  }
}
