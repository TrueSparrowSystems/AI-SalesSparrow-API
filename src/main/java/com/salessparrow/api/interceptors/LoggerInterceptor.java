package com.salessparrow.api.interceptors;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for logging
 *
 */
@Component
public class LoggerInterceptor implements HandlerInterceptor {

  /**
   * Intercept request and log request
   * 
   * @param request
   * @param response
   * @param handler
   * 
   * @return boolean
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String trackingId = request.getHeader("X-Tracking-Id");

    if (trackingId == null) {
      trackingId = java.util.UUID.randomUUID().toString();
    }
    
    MDC.put("trackingId", trackingId);
    return true;
  }
}
