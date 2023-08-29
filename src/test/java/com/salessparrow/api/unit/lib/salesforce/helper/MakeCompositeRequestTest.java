package com.salessparrow.api.unit.lib.salesforce.helper;

import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequestInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MakeCompositeRequestTest {

  @Mock
  private SalesforceRequest salesforceRequest;

  @Mock
  private SalesforceConstants salesforceConstants;

  @InjectMocks
  private MakeCompositeRequest makeCompositeRequest;

  @BeforeEach
  public void setUp() {
      MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testMakePostRequest() {

    List<CompositeRequestDto> compositeRequests = new ArrayList<>();
    CompositeRequestDto requestDto = new CompositeRequestDto("endpoint", "method", null);
    compositeRequests.add(requestDto);

    when(salesforceConstants.timeoutMillis()).thenReturn(5000);

    when(salesforceConstants.salesforceCompositeUrl(anyString())).thenReturn("composite_url");

    when(salesforceRequest.makeRequest(anyString(), any(SalesforceRequestInterface.class)))
      .thenReturn(new HttpClient.HttpResponse(200, "response", null, "application/json"));

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, "user_id");

    assertEquals(200, response.getStatusCode());
    assertEquals("response", response.getResponseBody());
  }
}

