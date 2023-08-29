package com.salessparrow.api.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.salessparrow.api.lib.globalConstants.ApiSource;

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
	 * @param request
	 * @param response
	 * @param handler
	 * @return boolean
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Set Source for app.This can be used to differentiate common web routes from app
		// routes in the service.
		request.setAttribute("api_source", ApiSource.APP);
		return true;
	}

}
