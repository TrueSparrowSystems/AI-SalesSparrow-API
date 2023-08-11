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

  public String getCookieValue(User user, String userKind, String decryptedSalt) {

    Integer currentTimestamp = (int) (System.currentTimeMillis() / 1000);
    String cookieToken = getCookieToken(user, decryptedSalt, currentTimestamp);

    if (user.getExternalUserId() == null) {
      throw new CustomException(
          new ErrorObject(
              "l_ch_gcv_1",
              "internal_server_error",
              "User is null"));
    }

    return CookieConstants.LATEST_VERSION + ':' + user.getExternalUserId() + ':' + userKind + ':' + currentTimestamp
        + ':'
        + cookieToken;
  }

  public String getCookieToken(User user, String decryptedSalt, Integer timestamp) {

    String decryptedCookieToken = localCipher.decrypt(decryptedSalt, user.getCookieToken());
    String strSecret = CoreConstants.apiCookieSecret();
    String stringToSign = user.getExternalUserId() + ':' + timestamp + ':' + strSecret + ':'
        + decryptedCookieToken.substring(0, 16);
    String salt = user.getExternalUserId() + ':' + decryptedCookieToken.substring(decryptedCookieToken.length() - 16)
        + ':'
        + strSecret + ':' + timestamp;
    String encryptedCookieToken = localCipher.encrypt(salt, stringToSign);

    return encryptedCookieToken;
  }

  public HttpHeaders setCookieInHeaders(String cookieName, String cookieValue,
      HttpHeaders headers) {
    int cookieExpiryInSecond = CookieConstants.USER_LOGIN_COOKIE_EXPIRY_IN_SEC;

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

    headers = setCookieInHeaders(cookieName, cookieValue, headers);

    return headers;
  }
}
