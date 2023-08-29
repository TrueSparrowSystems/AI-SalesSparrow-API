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
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetIdentity;

@SpringBootTest
@Import({SalesforceGetIdentity.class})
public class SalesforceGetIdentityTest {

  @Autowired
  private SalesforceGetIdentity salesforceGetIdentity;

  @Test
  public void testGetUserIdentity_Success() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String instanceUrl = "https://example.com";
    String accessToken = "dummyAccessToken";

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + accessToken);

    String responseBody = "Mock Response Body";
    HttpResponse mockResponse = new HttpResponse();
    mockResponse.setResponseBody(responseBody);

    httpClientMockedStatic.when(() -> HttpClient.makeGetRequest(anyString(), anyMap(), anyInt()))
      .thenReturn(mockResponse);
    
    HttpResponse actualResponse = salesforceGetIdentity.getUserIdentity(instanceUrl, accessToken);

    // Assertions
    assertEquals(mockResponse.getResponseBody(), actualResponse.getResponseBody());

    httpClientMockedStatic.close();
  }

  @Test
  public void testGetUserIdentity_Exception() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String instanceUrl = "https://example.com";
    String accessToken = "dummyAccessToken";

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + accessToken);

    String responseBody = "Mock Response Body";
    HttpResponse mockResponse = new HttpResponse();
    mockResponse.setResponseBody(responseBody);

    httpClientMockedStatic.when(() -> HttpClient.makeGetRequest(anyString(), anyMap(), anyInt()))
      .thenThrow(new RuntimeException("Some error occurred"));

    CustomException exception = assertThrows(CustomException.class, () -> {
        salesforceGetIdentity.getUserIdentity(instanceUrl, accessToken);
    });

    // Assertions
    assertNotNull(exception);
    ErrorObject errorObject = exception.getErrorObject();
    assertNotNull(errorObject);
    assertEquals("l_s_w_sgi_gui_1", errorObject.getInternalErrorIdentifier());
    assertEquals("bad_request", errorObject.getApiErrorIdentifier());

    httpClientMockedStatic.close();
  }
}
