package com.salessparrow.api.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    if (trackingId == null) {
      trackingId = java.util.UUID.randomUUID().toString();
    }

    long startTime = System.currentTimeMillis();
    request.setAttribute("startTime", startTime);

    MDC.put("trackingId", trackingId);

    logger.info("Request Start: {} {}", request.getMethod(), request.getRequestURI());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    long startTime = (Long)request.getAttribute("startTime");    
    long endTime = System.currentTimeMillis();
    long executeTime = endTime - startTime;

    logger.info("Request Ended with {} in {}ms", response.getStatus(), executeTime);
  }
}
