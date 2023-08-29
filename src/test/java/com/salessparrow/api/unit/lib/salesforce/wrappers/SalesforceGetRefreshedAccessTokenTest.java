package com.salessparrow.api.unit.lib.salesforce.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetRefreshedAccessToken;

@SpringBootTest
@Import({SalesforceGetRefreshedAccessToken.class})
public class SalesforceGetRefreshedAccessTokenTest {

  @Autowired
  private SalesforceGetRefreshedAccessToken salesforceGetRefreshedAccessToken;

  @Test
  public void testSalesforceGetRefreshedAccessToken() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String refreshToken = "dummyRefreshToken";

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + refreshToken);

    String responseBody = "Mock Response Body";
    HttpResponse mockResponse = new HttpResponse();
    mockResponse.setResponseBody(responseBody);

    httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()))
      .thenReturn(mockResponse);
    
    HttpResponse actualResponse = salesforceGetRefreshedAccessToken.getRefreshedAccessToken(refreshToken);

    // Assertions
    assertEquals(mockResponse.getResponseBody(), actualResponse.getResponseBody());

    httpClientMockedStatic.close();
  }
}
