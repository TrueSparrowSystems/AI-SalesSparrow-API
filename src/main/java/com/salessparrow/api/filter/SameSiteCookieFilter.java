package com.salessparrow.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Filter to set SameSite cookie attribute
 *
 * If you want to implement SameSite attribute setting in an interceptor, you can do it,
 * but it won't be as efficient as doing it in a filter. That's because, by the time the
 * interceptor is called, some part of the request processing within the Spring MVC
 * framework has already happened.
 *
 */
public class SameSiteCookieFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) {
	}

	/**
	 * Set SameSite cookie attribute. Override the doFilter method to wrap the response
	 * object with a custom wrapper.
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 * @return void
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		SameSiteResponseWrapper wrappedResponse = new SameSiteResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, wrappedResponse);
	}

	@Override
	public void destroy() {
	}

	/**
	 * Custom wrapper to intercept the setHeader calls and add SameSite attribute to the
	 * cookie. Set SameSite=lax lax will ensure that cookies are sent only if the request
	 * originates from the same site. It will also send the cookie if the user is
	 * navigating from an external site, but only if the URL in the browser’s address bar
	 * matches the URL of the current site.
	 */
	public class SameSiteResponseWrapper extends HttpServletResponseWrapper {

		/**
		 * Constructor
		 * @param response
		 * @return void
		 */
		public SameSiteResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		/**
		 * Override the addHeader method to add SameSite attribute to the cookie
		 * @param name
		 * @param value
		 * @return void
		 *
		 */
		@Override
		public void addHeader(String name, String value) {
			if (name.equalsIgnoreCase("set-cookie") && !value.toLowerCase().contains("samesite")) {
				value = String.format("%s; SameSite=lax", value);
			}
			super.addHeader(name, value);
		}

		/**
		 * Override the setHeader method to add SameSite attribute to the cookie
		 * @param name
		 * @param value
		 * @return void
		 *
		 */
		@Override
		public void setHeader(String name, String value) {

			if (name.equalsIgnoreCase("set-cookie") && !value.toLowerCase().contains("samesite")) {
				value = String.format("%s; SameSite=lax", value);
			}
			super.setHeader(name, value);
		}

	}

}
