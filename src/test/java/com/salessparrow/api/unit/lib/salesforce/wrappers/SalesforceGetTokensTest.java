package com.salessparrow.api.unit.lib.salesforce.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetTokens;

@SpringBootTest
@Import({SalesforceGetTokens.class})
public class SalesforceGetTokensTest {

  @Autowired
  private SalesforceGetTokens salesforceGetTokens;

  @Test
  public void testSalesforceGetTokens_Success() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String code = "dummyCode";
    String redirectUri = "dummyRedirectUri";

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Dummy Bearer Header");

    String responseBody = "Mock Response Body";
    HttpResponse mockResponse = new HttpResponse();
    mockResponse.setResponseBody(responseBody);

    httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()))
      .thenReturn(mockResponse);
    
    HttpResponse actualResponse = salesforceGetTokens.getTokens(code, redirectUri);

    // Assertions
    assertEquals(mockResponse.getResponseBody(), actualResponse.getResponseBody());

    httpClientMockedStatic.close();
  }

  @Test
  public void testSalesforceGetTokens_Exception() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String code = "dummyCode";
    String redirectUri = "dummyRedirectUri";

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Dummy Bearer Header");

    String responseBody = "Mock Response Body";
    HttpResponse mockResponse = new HttpResponse();
    mockResponse.setResponseBody(responseBody);

    httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()))
      .thenThrow(new RuntimeException("Some error occurred"));

    CustomException exception = assertThrows(CustomException.class, () -> {
        salesforceGetTokens.getTokens(code, redirectUri);
    });

    // Assertions
    assertNotNull(exception);
    ParamErrorObject paramErrorObject = exception.getParamErrorObject();
    assertNotNull(paramErrorObject);
    assertEquals("l_s_w_sgt_gt_1", paramErrorObject.getInternalErrorIdentifier());

    httpClientMockedStatic.close();
  }
}
