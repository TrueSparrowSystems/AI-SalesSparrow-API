package com.salessparrow.api.lib.wrappers;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Custom request wrapper to sanitize the request body
 */
public class SanitizedRequestWrapper extends HttpServletRequestWrapper {
	private final String sanitizedBody;

	private Map<String, String> sanitizedParams;

	private Map<String, String> sanitizedHeaders;

	public SanitizedRequestWrapper(HttpServletRequest request, String sanitizedBody) {
		super(request);
		this.sanitizedBody = StringEscapeUtils.unescapeHtml4(sanitizedBody);
		this.sanitizedParams = new HashMap<>();
		this.sanitizedHeaders = new HashMap<>();
	}

	/**
	 * Method to get the request body as buffered reader
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(
			new InputStreamReader(
				new ByteArrayInputStream(sanitizedBody.getBytes())));
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
	 * 
	 * @param key
	 * @param value
	 * 
	 * @return void
	 */
	public void setParameter(String key, String value) {
		this.sanitizedParams.put(key, value);
	}

	/**
	 * Method to get the request parameter value by 
	 * key from the sanitized params map if present 
	 * else from the super class
	 * 
	 * @param key - Request parameter key
	 * 
	 * @return String - Request parameter value
	 */
	@Override
	public String getParameter(String key) {
		return this.sanitizedParams.getOrDefault(key, super.getParameter(key));
	}

	/**
	 * Method to get the request parameter map
	 * 
	 * @param key - header key
	 * @param value - header value
	 *
	 * @return void
	 */
	public void setHeader(String key, String value) {
		this.sanitizedHeaders.put(key, value);
	}

	@Override
	public String getHeader(String key) {
		return this.sanitizedHeaders.getOrDefault(key, super.getHeader(key));
	}
}

