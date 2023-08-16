package com.salessparrow.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.salessparrow.api.interceptors.LoggerInterceptor;
import com.salessparrow.api.interceptors.UserAuthInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  
  @Autowired
  private final LoggerInterceptor loggerInterceptor;

  @Autowired
  private final UserAuthInterceptor userAuthInterceptor;

  public InterceptorConfig(UserAuthInterceptor userAuthInterceptor, LoggerInterceptor loggerInterceptor) {
    this.userAuthInterceptor = userAuthInterceptor;
    this.loggerInterceptor = loggerInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loggerInterceptor)
        .addPathPatterns("/**");

    registry.addInterceptor(userAuthInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/api/v1/auth/salesforce/**");
  }
}