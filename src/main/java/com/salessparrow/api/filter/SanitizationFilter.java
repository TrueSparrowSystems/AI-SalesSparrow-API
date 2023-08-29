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

    private final PolicyFactory policy = new HtmlPolicyBuilder()
				.allowElements(
					"a", "label", "h1", "h2", "h3", "h4", "h5", "h6",
					"p", "i", "b", "u", "strong", "em", "strike", "code", "hr", "br", "div",
					"table", "thead", "caption", "tbody", "tr", "th", "td", "pre")
				.allowUrlProtocols("https")
				.allowAttributes("href").onElements("a")
				.allowAttributes("target").onElements("a")
				.allowAttributes("class").globally()
				.allowAttributes("id").globally()
				.disallowElements(
						"script", "iframe", "object", "embed", "form", "input", "button", "select",
						"textarea", "style", "link", "meta", "base"
				)
				.toFactory();

    @Override
    public void init(FilterConfig filterConfig) {
    }

		/**
		 * Method to sanitize the request
		 * 
		 * @param servletRequest - Servlet request object
		 * @param servletResponse - Servlet response object
		 * @param chain - Filter chain - Filter chain
		 * 
		 * @throws IOException - IOException
		 * @throws ServletException - ServletException
		 * 
		 * @return void
		 */
    @Override
    public void doFilter(
			ServletRequest servletRequest, 
			ServletResponse servletResponse, 
			FilterChain chain
		) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			sanitizeRequestBody(request);
			sanitizeRequestParams();
			sanitizeRequestHeaders();

			chain.doFilter(sanitizedRequest, servletResponse);
    }

		/**
		 * Method to sanitize the request body
		 * 
		 * @param request - Servlet request object
		 * 
		 * @return void
		 */
		private void sanitizeRequestBody(HttpServletRequest request) {
			String originalBody = getRequestBody(request);
			String sanitizedBody = sanitizeHtml(originalBody);
			this.sanitizedRequest = new SanitizedRequestWrapper(request, sanitizedBody);
		}

		/**
		 * Method to sanitize the request params
		 * 
		 * @return void
		 */
		private void sanitizeRequestParams() {
			this.sanitizedRequest.getParameterMap().forEach((key, value) -> {
				String sanitizedValue = sanitizeHtml(value[0]);
				this.sanitizedRequest.setParameter(key, sanitizedValue);
			});
		}

		/**
		 * Method to sanitize the request headers
		 * 
		 * @return void
		 */
		private void sanitizeRequestHeaders() {
			Enumeration<String> headerNames = this.sanitizedRequest.getHeaderNames();

			if (headerNames != null && headerNames.hasMoreElements()) {
				this.sanitizedRequest.getHeaderNames().asIterator().forEachRemaining(headerName -> {
					String sanitizedValue = sanitizeHtml(this.sanitizedRequest.getHeader(headerName));
					this.sanitizedRequest.setHeader(headerName, sanitizedValue);
				});
			}
		}

		/**
		 * Method to get the request body
		 * 
		 * @param request - Servlet request object
		 * @return String - Request body
		 */
    private String getRequestBody(HttpServletRequest request) {
			try {
				BufferedReader reader = request.getReader();
				String line;
				StringBuilder requestBody = new StringBuilder();
				while ((line = reader.readLine()) != null) {
						requestBody.append(line);
				}
				return requestBody.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
    }

		/**
		 * Method to sanitize the html
		 * 
		 * @param input - Input string
		 * @return String - Sanitized string
		 */
    public String sanitizeHtml(String input) {
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
