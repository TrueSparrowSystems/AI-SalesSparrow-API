package com.salessparrow.api.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.jsoup.nodes.Entities.EscapeMode;

import com.salessparrow.api.lib.wrappers.SanitizedRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Class to sanitize the request
 */
public class SanitizationFilter implements Filter {

	// private SanitizedRequestWrapper sanitizedRequest;

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
		SanitizedRequestWrapper sanitizedRequest = sanitizeRequestBody(request);
		sanitizeRequestParams(request, sanitizedRequest);
		sanitizeRequestHeaders(request, sanitizedRequest);

		chain.doFilter(sanitizedRequest, servletResponse);
	}

	/**
	 * Method to sanitize the request body
	 * @param request - Servlet request object
	 * @return void
	 */
	private SanitizedRequestWrapper sanitizeRequestBody(HttpServletRequest request) {
		String originalBody = getRequestBody(request);
		String sanitizedBody = sanitizeHtml(originalBody);

		return new SanitizedRequestWrapper(request, sanitizedBody);
	}

	/**
	 * Method to sanitize the request params
	 * @return void
	 */
	private void sanitizeRequestParams(HttpServletRequest request, SanitizedRequestWrapper sanitizedRequest) {
		request.getParameterMap().forEach((key, values) -> {
			for (int index = 0; index < values.length; index++) {
				String sanitizedValue = sanitizeHtml(values[index]);
				sanitizedRequest.setParameter(key, sanitizedValue);
			}
		});
	}

	/**
	 * Method to sanitize the request headers
	 * @return void
	 */
	private void sanitizeRequestHeaders(HttpServletRequest request, SanitizedRequestWrapper sanitizedRequest) {
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames != null && headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);

			while (headerValues != null && headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				String sanitizedValue = sanitizeHtml(headerValue);
				sanitizedRequest.setHeader(headerName, sanitizedValue);
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
	 * Sanitizes a given HTML string by removing all HTML tags and attributes, leaving
	 * only the text content. Also handles basic HTML entity escaping using the 'base'
	 * escape mode.
	 *
	 * This function uses the Jsoup library's 'clean' method with a 'none' safelist, which
	 * means no HTML tags are allowed and will be removed.
	 *
	 * The function also disables pretty-printing to ensure the resulting HTML is not
	 * reformatted, and sets the escape mode to 'base' to handle basic HTML entities like
	 * &amp; and &lt;.
	 *
	 * If the input is null or empty, the function returns an empty string.
	 * @param input The original HTML string that needs to be sanitized.
	 * @return A sanitized version of the input string, safe for display as text.
	 */
	private String sanitizeHtml(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		Safelist safelist = Safelist.none();

		Document.OutputSettings outputSettings = new Document.OutputSettings();
		outputSettings.prettyPrint(false);
		outputSettings.escapeMode(EscapeMode.base);

		String sanitizedInput = Jsoup.clean(input, "", safelist, outputSettings);

		return sanitizedInput;
	}

	/**
	 * Method to destroy the filter
	 */
	@Override
	public void destroy() {
	}

}
