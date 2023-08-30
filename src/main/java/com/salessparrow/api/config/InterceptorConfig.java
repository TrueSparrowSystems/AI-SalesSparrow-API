package com.salessparrow.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.salessparrow.api.interceptors.LoggerInterceptor;
import com.salessparrow.api.interceptors.JsonOnlyInterceptor;
import com.salessparrow.api.interceptors.UserAuthInterceptor;
import com.salessparrow.api.interceptors.V1Interceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private final LoggerInterceptor loggerInterceptor;

	@Autowired
	private final JsonOnlyInterceptor jsonOnlyInterceptor;

	@Autowired
	private final V1Interceptor V1Interceptor;

	@Autowired
	private final UserAuthInterceptor userAuthInterceptor;

	public InterceptorConfig(UserAuthInterceptor userAuthInterceptor, JsonOnlyInterceptor jsonOnlyInterceptor,
			LoggerInterceptor loggerInterceptor, V1Interceptor V1Interceptor) {
		this.userAuthInterceptor = userAuthInterceptor;
		this.loggerInterceptor = loggerInterceptor;
		this.V1Interceptor = V1Interceptor;
		this.jsonOnlyInterceptor = jsonOnlyInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/* Add logger interceptor to all the routes */
		registry.addInterceptor(loggerInterceptor).addPathPatterns("/**");

		/* Add json only interceptor to all the routes */
		registry.addInterceptor(jsonOnlyInterceptor).addPathPatterns("/**");

		/* Add v1 interceptor only to all the routes */
		registry.addInterceptor(V1Interceptor).addPathPatterns("/api/v1/**");

		/* Add user auth interceptor to all the routes except the ones below */
		registry.addInterceptor(userAuthInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/v1/auth/salesforce/**")
			.excludePathPatterns("/api/v1/health-check");
	}

}