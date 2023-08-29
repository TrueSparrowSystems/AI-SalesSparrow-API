package com.salessparrow.api.unit.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.salessparrow.api.filter.SanitizationFilter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SanitizationFilterTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain chain;

  private SanitizationFilter filter;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    filter = new SanitizationFilter();
    filter.init(null);
  }

  @Test
  public void testSanitizeHtmlRemovesScriptTags() {
    String input = "<script>alert('Hi');</script>";
    String expected = ""; // Expecting empty string after removing script tags

    String sanitized = filter.sanitizeHtml(input);
    assertEquals(expected, sanitized);
  }

  @Test
  public void testSanitizeHtmlAllowsSafeTags() {
    String input = "<p>Paragraph</p>";
    String expected = "<p>Paragraph</p>"; // The string should remain unchanged

    String sanitized = filter.sanitizeHtml(input);
    assertEquals(expected, sanitized);
  }

  @Test
  public void testDoFilterSanitizesScriptTags() throws Exception {
    ArgumentCaptor<ServletRequest> argumentCaptor = ArgumentCaptor.forClass(ServletRequest.class);
    
    String input = "<script>alert('Hi');</script>";
    StringReader stringReader = new StringReader(input);
    BufferedReader reader = new BufferedReader(stringReader);

    when(request.getReader()).thenReturn(reader);
    filter.doFilter(request, response, chain);

    verify(chain).doFilter(argumentCaptor.capture(), eq(response));
    
    ServletRequest capturedRequest = argumentCaptor.getValue();
    String sanitizedBody = convertInputStreamToString(capturedRequest.getInputStream());
    
    assertEquals("", sanitizedBody);
  }

  @Test
  public void testDoFilterAllowsSafeTags() throws Exception {
    ArgumentCaptor<ServletRequest> argumentCaptor = ArgumentCaptor.forClass(ServletRequest.class);

    String input = "<p>Paragraph</p>";
    StringReader stringReader = new StringReader(input);
    BufferedReader reader = new BufferedReader(stringReader);

    when(request.getReader()).thenReturn(reader);
    filter.doFilter(request, response, chain);

    verify(chain).doFilter(argumentCaptor.capture(), eq(response));
    
    ServletRequest capturedRequest = argumentCaptor.getValue();
    String sanitizedBody = convertInputStreamToString(capturedRequest.getInputStream());
    
    assertEquals("<p>Paragraph</p>", sanitizedBody);
  }

  private String convertInputStreamToString(InputStream inputStream) throws Exception {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      return sb.toString();
    }
  }
}

