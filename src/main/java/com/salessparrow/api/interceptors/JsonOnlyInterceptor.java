package com.salessparrow.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for strict application/json Content-Type header
 *
 */
@Component
public class JsonOnlyInterceptor implements HandlerInterceptor {

	/**
	 * Intercept request and validate Content-type header Only application/json is allowed
	 * @param request
	 * @param response
	 * @param handler
	 * @return boolean
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String contentType = request.getHeader("Content-Type");
		if (contentType != null && !contentType.contains("application/json")) {
			throw new CustomException(new ErrorObject("i_joi_ph_1", "unsupported_media_type",
					"Content-Type header must be application/json"));
		}

		return true;
	}

}
