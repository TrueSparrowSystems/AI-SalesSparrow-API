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
  private final UserAuthInterceptor userAuthInterceptor;

  @Autowired
  private final LoggerInterceptor loggerInterceptor;

  public InterceptorConfig(UserAuthInterceptor userAuthInterceptor, LoggerInterceptor loggerInterceptor) {
    this.userAuthInterceptor = userAuthInterceptor;
    this.loggerInterceptor = loggerInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userAuthInterceptor)
        .addPathPatterns("/api/v1/users/**")
        .addPathPatterns("/api/v1/auth/logout")
        .addPathPatterns("/api/v1/accounts/**");

    registry.addInterceptor(loggerInterceptor)
        .addPathPatterns("/**");
  }
}