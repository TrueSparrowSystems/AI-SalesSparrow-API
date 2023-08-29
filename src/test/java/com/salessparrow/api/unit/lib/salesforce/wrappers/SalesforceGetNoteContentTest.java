package com.salessparrow.api.unit.lib.salesforce.wrappers;

import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequestInterface;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetNoteContent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SalesforceGetNoteContentTest {

  @Autowired
  private SalesforceGetNoteContent salesforceGetNoteContent;

  @Mock
  private SalesforceConstants salesforceConstants;

  @Mock
  private SalesforceRequest salesforceRequest;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(salesforceGetNoteContent, "salesforceConstants", salesforceConstants);
    ReflectionTestUtils.setField(salesforceGetNoteContent, "salesforceOauthRequest", salesforceRequest);
  }

  @Test
  public void testGetNoteContent() {
    String noteId = "note123";
    String salesforceUserId = "user123";

    // Mock SalesforceConstants
    when(salesforceConstants.timeoutMillis()).thenReturn(5000);

    // Mock SalesforceRequest
    when(salesforceRequest.makeRequest(eq(salesforceUserId), any(SalesforceRequestInterface.class)))
      .thenReturn(new HttpClient.HttpResponse(200, "Note Content", null, "text/plain"));

    // Test the method
    HttpClient.HttpResponse response = salesforceGetNoteContent.getNoteContent(noteId, salesforceUserId);

    // Verify that the methods were called with the expected parameters
    verify(salesforceConstants).timeoutMillis();
    verify(salesforceRequest).makeRequest(eq(salesforceUserId), any(SalesforceRequestInterface.class));

    // Verify the response
    assertEquals(200, response.getStatusCode());
    assertEquals("Note Content", response.getResponseBody());
    assertEquals("text/plain", response.getContentType());
  }
}
