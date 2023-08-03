package com.salessparrow.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.salessparrow.api.interceptors.LoggerInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Autowired
  private final LoggerInterceptor loggerInterceptor;

  public InterceptorConfig(LoggerInterceptor loggerInterceptor) {
    this.loggerInterceptor = loggerInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(loggerInterceptor)
        .addPathPatterns("/**");
  }
}