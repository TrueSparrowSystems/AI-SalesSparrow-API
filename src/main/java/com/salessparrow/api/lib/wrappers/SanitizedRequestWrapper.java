package com.salessparrow.api.lib.wrappers;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom request wrapper to sanitize the request body
 */
public class SanitizedRequestWrapper extends HttpServletRequestWrapper {

	Logger logger = LoggerFactory.getLogger(SanitizedRequestWrapper.class);
	
	private final String sanitizedBody;

	private Map<String, List<String>> sanitizedParams;

	private Map<String, List<String>> sanitizedHeaders;

	public SanitizedRequestWrapper(HttpServletRequest request, String sanitizedBody) {
		super(request);
		logger.info("SanitizedRequestWrapper sanitizedBody: ", sanitizedBody);
		this.sanitizedBody = sanitizedBody;
		this.sanitizedParams = new HashMap<>();
		this.sanitizedHeaders = new HashMap<>();
	}

	/**
	 * Method to get the request body as buffered reader
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sanitizedBody.getBytes())));
	}

	/**
	 * Method to get the request body as input stream
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sanitizedBody.getBytes());

		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return byteArrayInputStream.available() == 0;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				throw new UnsupportedOperationException("Not implemented");
			}
		};
	}

	/**
	 * Method to set the request parameter value by key
	 * @param key
	 * @param value
	 * @return void
	 */
	public void setParameter(String key, String value) {
		List<String> values = this.sanitizedParams.getOrDefault(key, new ArrayList<String>());
		values.add(value);
		this.sanitizedParams.put(key, values);
	}

	/**
	 * Method to get the request parameter value by key from the sanitized params map.
	 * @param key - parameter key
	 * @return String - parameter value
	 */
	@Override
	public String getParameter(String key) {
		List<String> values = this.sanitizedParams.get(key);
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		}

		return null;
	}

	/**
	 * Method to get the request parameter map from the sanitized params map.
	 * @return Map<String, String[]> - parameter map
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> sanitizedParamsMap = new HashMap<>();
		this.sanitizedParams.forEach((key, values) -> {
			String[] stringValues = values.toArray(new String[0]);
			sanitizedParamsMap.put(key, stringValues);
		});

		return sanitizedParamsMap;
	}

	/**
	 * Method to get the request parameter names from the sanitized params map.
	 * @return Enumeration<String> - parameter names
	 */
	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.sanitizedParams.keySet());
	}

	/**
	 * Method to get the request parameter values by key from the sanitized params map.
	 * @param key - parameter key
	 * @return String[] - parameter values
	 */
	@Override
	public String[] getParameterValues(String key) {
		List<String> values = this.sanitizedParams.get(key);
		if (values != null && !values.isEmpty()) {
			return values.toArray(new String[0]);
		}
		return null;
	}

	/**
	 * Method to get the request parameter map
	 * @param key - header key
	 * @param value - header value
	 * @return void
	 */
	public void setHeader(String key, String value) {
		List<String> headerValues = this.sanitizedHeaders.getOrDefault(key, new ArrayList<String>());
		headerValues.add(value);
		this.sanitizedHeaders.put(key, headerValues);
	}

	/**
	 * Method to get the request header value by key from the sanitized headers map.
	 * @param key - header key
	 * @return String - header value
	 */
	@Override
	public String getHeader(String key) {
		List<String> values = this.sanitizedHeaders.get(key);
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		}
		return null;
	}

	/**
	 * Method to get the request header names from the sanitized headers map.
	 * @return Enumeration<String> - header names
	 * @return void
	 */
	@Override
	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(this.sanitizedHeaders.keySet());
	}

	/**
	 * Method to get the request header values by key from the sanitized headers map.
	 * @param key - header key
	 * @return Enumeration<String> - header values
	 */
	@Override
	public Enumeration<String> getHeaders(String key) {
		List<String> values = this.sanitizedHeaders.get(key);
		if (values != null && !values.isEmpty()) {
			return Collections.enumeration(values);
		}
		return Collections.enumeration(Collections.emptyList());
	}

}
