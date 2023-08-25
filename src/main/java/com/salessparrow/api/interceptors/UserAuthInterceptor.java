package com.salessparrow.api.interceptors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.UserLoginCookieAuth;
import com.salessparrow.api.lib.globalConstants.CookieConstants;

/**
 * Interceptor for user authentication
 *
 */
@Component
public class UserAuthInterceptor implements HandlerInterceptor {

  @Autowired
  private UserLoginCookieAuth userLoginCookieAuth;

  Logger logger = LoggerFactory.getLogger(UserAuthInterceptor.class);

  @Autowired
  private CookieHelper cookieHelper;

  /**
   * Intercept request and validate user login cookie
   *
   * @param request
   * @param response
   * @param handler
   *
   * @return boolean
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String cookieValue = getCookieValue(request);
    String reqApiSource = (String) request.getAttribute("api_source");
    
    Map<String, Object> userLoginCookieAuthRes = userLoginCookieAuth.validateAndSetCookie(cookieValue, reqApiSource);

    User currentUser = (User) userLoginCookieAuthRes.get("current_user");
    request.setAttribute("current_user", currentUser);

    String userLoginCookieValue = (String) userLoginCookieAuthRes.get("userLoginCookieValue");

    String cookieName = CookieConstants.USER_LOGIN_COOKIE_NAME;

    HttpHeaders headers = new HttpHeaders();
    headers = cookieHelper.setCookieInHeaders(cookieName, userLoginCookieValue, headers);

    response.addHeader(HttpHeaders.SET_COOKIE, headers.getFirst(HttpHeaders.SET_COOKIE));

    return true;
  }

  /**
   * Get cookie value from request
   *
   * @param request
   * @param cookieName
   *
   * @return String
   */
  private String getCookieValue(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(CookieConstants.USER_LOGIN_COOKIE_NAME)) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

}
