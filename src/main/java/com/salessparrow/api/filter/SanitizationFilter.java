package com.salessparrow.api.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import com.salessparrow.api.lib.wrappers.SanitizedRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Class to sanitize the request
 */
public class SanitizationFilter implements Filter {

	private SanitizedRequestWrapper sanitizedRequest;

	private final PolicyFactory policy = new HtmlPolicyBuilder().toFactory();

	@Override
	public void init(FilterConfig filterConfig) {
	}

	/**
	 * Method to sanitize the request
	 * @param servletRequest - Servlet request object
	 * @param servletResponse - Servlet response object
	 * @param chain - Filter chain - Filter chain
	 * @throws IOException - IOException
	 * @throws ServletException - ServletException
	 * @return void
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest)) {
			throw new ServletException("Can only process HttpServletRequest");
		}
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		sanitizeRequestBody(request);
		sanitizeRequestParams(request);
		sanitizeRequestHeaders(request);

		chain.doFilter(sanitizedRequest, servletResponse);
	}

	/**
	 * Method to sanitize the request body
	 * @param request - Servlet request object
	 * @return void
	 */
	private void sanitizeRequestBody(HttpServletRequest request) {
		String originalBody = getRequestBody(request);
		String sanitizedBody = sanitizeHtml(originalBody);
		this.sanitizedRequest = new SanitizedRequestWrapper(request, sanitizedBody);
	}

	/**
	 * Method to sanitize the request params
	 * @return void
	 */
	private void sanitizeRequestParams(HttpServletRequest request) {
		request.getParameterMap().forEach((key, values) -> {
			for (int index = 0; index < values.length; index++) {
				String sanitizedValue = sanitizeHtml(values[index]);
				this.sanitizedRequest.setParameter(key, sanitizedValue);
			}
		});
	}

	/**
	 * Method to sanitize the request headers
	 * @return void
	 */
	private void sanitizeRequestHeaders(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames != null && headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);

			while (headerValues != null && headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				String sanitizedValue = sanitizeHtml(headerValue);
				this.sanitizedRequest.setHeader(headerName, sanitizedValue);
			}
		}
	}

	/**
	 * Method to get the request body
	 * @param request - Servlet request object
	 * @return String - Request body
	 */
	private String getRequestBody(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			String line;
			// TODO: Consider using a more efficient way to read the request body, such as
			// streaming
			StringBuilder requestBody = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}
			return requestBody.toString();
		}
		catch (IOException e) {
			throw new RuntimeException("Error reading request body: ", e.getCause());
		}
	}

	/**
	 * Method to sanitize the html
	 * @param input - Input string
	 * @return String - Sanitized string
	 */
	private String sanitizeHtml(String input) {
		String sanitizedInput = policy.sanitize(input);
		return sanitizedInput;
	}

	/**
	 * Method to destroy the filter
	 */
	@Override
	public void destroy() {
	}

}
