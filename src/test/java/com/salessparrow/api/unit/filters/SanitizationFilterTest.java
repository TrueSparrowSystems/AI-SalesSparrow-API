package com.salessparrow.api.unit.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.salessparrow.api.filter.SanitizationFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

class SanitizationFilterTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@Mock
	private ServletRequest nonHttpRequest;

	@InjectMocks
	private SanitizationFilter sanitizationFilter;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testDoFilter() throws Exception {
    // Arrange: Setup common request behaviors with unsafe body and params
    when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader("<script>alert('malicious code')</script>"))
    );
    when(request.getParameterMap()).thenReturn(mockedParamMap(true));

    // Act: Pass through sanitization filter
    sanitizationFilter.doFilter(request, response, filterChain);

    // Assert: Check that the filterChain's doFilter is called
    ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
    verify(filterChain).doFilter(requestCaptor.capture(), eq(response));
    HttpServletRequest sanitizedRequest = requestCaptor.getValue();

    // Assert: Check that request body is sanitized
    String sanitizedBody = getRequestBody(sanitizedRequest);
    assertFalse(sanitizedBody.contains("<script>"), "Request body should be sanitized");

    // Assert: Check that request parameters are sanitized
    assertEquals("", sanitizedRequest.getParameter("param1"), "Parameter should be sanitized");
	}

	@Test
	void testRequestBodySanitization() throws Exception {
		when(request.getReader()).thenReturn(
		new BufferedReader(
		new StringReader("<script>alert('malicious code')</script>")));
		sanitizationFilter.doFilter(request, response, filterChain);

		ArgumentCaptor<HttpServletRequest> requestCaptor =
		ArgumentCaptor.forClass(HttpServletRequest.class);
		verify(filterChain).doFilter(requestCaptor.capture(), eq(response));
		HttpServletRequest sanitizedRequest = requestCaptor.getValue();

		String sanitizedBody = getRequestBody(sanitizedRequest);
		assertFalse(sanitizedBody.contains("<script>"));
	}

	@Test
	void testRequestParamsSanitization() throws Exception {
		when(request.getReader()).thenReturn(
			new BufferedReader(new StringReader("some request body")));
    when(request.getParameterMap()).thenReturn(mockedParamMap(false));
    sanitizationFilter.doFilter(request, response, filterChain);

    ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
    verify(filterChain).doFilter(requestCaptor.capture(), eq(response));
    HttpServletRequest sanitizedRequest = requestCaptor.getValue();

    Map<String, String[]> sanitizedParams = sanitizedRequest.getParameterMap();
    assertEquals("safe_value1", sanitizedParams.get("param1")[0]);
	}

	@Test
	void testRequestHeadersSanitization() throws Exception {
		when(request.getReader()).thenReturn(
			new BufferedReader(new StringReader("some request body")));
		// Arrange: Mock the behavior of the request object to return unsafe headers
		when(request.getHeaderNames()).thenReturn(mockedUnsafeHeaderNames());

		// Act: Call the method under test
		sanitizationFilter.doFilter(request, response, filterChain);

		// Assert: Capture the request and examine it
		ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
		verify(filterChain).doFilter(requestCaptor.capture(), eq(response));
		HttpServletRequest sanitizedRequest = requestCaptor.getValue();

		Enumeration<String> sanitizedHeaderNames = sanitizedRequest.getHeaderNames();

		assertFalse(sanitizedHeaderNames.hasMoreElements());
	}

	@Test
	void testIOException() throws Exception {
		assertThrows(RuntimeException.class, () -> {
			when(request.getReader()).thenThrow(new IOException("Test IOException"));
			sanitizationFilter.doFilter(request, response, filterChain);
		}, "Expected doFilter to throw RuntimeException");

		// Verify that the filterChain's doFilter method is never called.
		verify(filterChain, never()).doFilter(any(), any());
	}

	@Test
	void testNonHttpRequest() throws Exception {
		assertThrows(ServletException.class, () -> {
			sanitizationFilter.doFilter(nonHttpRequest, response, filterChain);
		}, "Expected doFilter to throw ServletException");

		verify(filterChain, never()).doFilter(any(), any());
	}

	private static Enumeration<String> mockedUnsafeHeaderNames() {
		Vector<String> headerNames = new Vector<>();
		headerNames.add("Unsafe-Header1: <script>");
		headerNames.add("Unsafe-Header2: <img src=x onerror=alert('img')>");
		return headerNames.elements();
	}

	private static Map<String, String[]> mockedParamMap(boolean unsafe) {
		Map<String, String[]> paramMap = new HashMap<>();
		if (unsafe) {
			paramMap.put("param1", new String[] { "<script>unsafe_value</script>" });
			paramMap.put("param2", new String[] { "<img src=x onerror=alert('img')>" });
		}
		else {
			paramMap.put("param1", new String[] { "safe_value1" });
			paramMap.put("param2", new String[] { "safe_value2" });
		}
		return paramMap;
	}

	public String getRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		if (reader == null) {
			return "";
		}

		return reader.lines().collect(Collectors.joining(System.lineSeparator()));
	}

}
