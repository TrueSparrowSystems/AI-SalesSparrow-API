package com.salessparrow.api.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for v1 routes
 *
 */
@Component
public class V1Interceptor implements HandlerInterceptor {

  /**
   * Intercept all /v1/* request and add middleware logic here
   * 
   * @param request
   * @param response
   * @param handler
   * 
   * @return boolean
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    Logger logger = LoggerFactory.getLogger(V1Interceptor.class);

    //Log App specific Headers
    logger.info("Headers device-uuid:{} device-name:{} device-os:{} build-number:{}", 
    request.getHeader("x-salessparrow-device-uuid"), request.getHeader("x-salessparrow-device-name"),
    request.getHeader("x-salessparrow-device-os"), request.getHeader("x-salessparrow-build-number"));

    return true;
  }
}
