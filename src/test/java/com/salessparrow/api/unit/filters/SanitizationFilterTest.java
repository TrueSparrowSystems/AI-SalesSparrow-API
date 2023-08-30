package com.salessparrow.api.unit.filters;

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
		mockCommonRequestBehaviors();
		sanitizationFilter.doFilter(request, response, filterChain);
		verify(filterChain).doFilter(any(), eq(response));
	}

	@Test
	void testRequestBodySanitization() throws Exception {
		mockCommonRequestBehaviors();
		sanitizationFilter.doFilter(request, response, filterChain);
		verify(request).getReader();
	}

	@Test
	void testRequestParamsSanitization() throws Exception {
		mockCommonRequestBehaviors();
		sanitizationFilter.doFilter(request, response, filterChain);
		verify(request).getParameterMap();
	}

	@Test
	void testRequestHeadersSanitization() throws Exception {
		mockCommonRequestBehaviors();
		sanitizationFilter.doFilter(request, response, filterChain);
		verify(request).getHeaderNames();
	}

	@Test
	void testIOException() throws Exception {
		assertThrows(RuntimeException.class, () -> {
			when(request.getReader()).thenThrow(new IOException("Test IOException"));
			sanitizationFilter.doFilter(request, response, filterChain);
		}, "Expected doFilter to throw RuntimeException");
	}

	@Test
	void testNonHttpRequest() {
		assertThrows(ServletException.class, () -> {
			sanitizationFilter.doFilter(nonHttpRequest, response, filterChain);
		}, "Expected doFilter to throw ServletException");
	}

	// Helper methods
	private void mockCommonRequestBehaviors() throws IOException {
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader("some request body")));
    when(request.getParameterMap()).thenReturn(mockedParamMap());
    when(request.getHeaderNames()).thenReturn(mockedHeaderNames());
  }

	private static Enumeration<String> mockedHeaderNames() {
		Vector<String> headerNames = new Vector<>();
		headerNames.add("Header1");
		headerNames.add("Header2");
		return headerNames.elements();
	}

	private static Map<String, String[]> mockedParamMap() {
		Map<String, String[]> paramMap = new HashMap<>();
		paramMap.put("param1", new String[] { "value1" });
		paramMap.put("param2", new String[] { "value2" });
		return paramMap;
	}

}
