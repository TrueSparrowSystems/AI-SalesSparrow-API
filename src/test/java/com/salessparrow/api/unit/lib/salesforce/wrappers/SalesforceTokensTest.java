package com.salessparrow.api.unit.lib.salesforce.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceTokens;

@SpringBootTest
@Import({ SalesforceTokens.class })
public class SalesforceTokensTest {

  @MockBean
  private HttpClient httpClientMock;

  @Autowired
  private SalesforceTokens salesforceTokens;

  @Test
  public void testRevokeTokensSuccess() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String instanceUrl = "https://example.com";
    String refreshToken = "your-refresh-token";

    HttpResponse expectedResponse = new HttpResponse();
    expectedResponse.setResponseBody("");

    httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), any(), anyInt()))
        .thenReturn(expectedResponse);

    HttpResponse response = salesforceTokens.revokeTokens(instanceUrl, refreshToken);

    assertEquals(expectedResponse, response);

    httpClientMockedStatic.close();
  }

  @Test
  public void testRevokeTokensFailure() throws Exception {
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    String instanceUrl = "https://example.com";
    String refreshToken = "invalid-refresh-token";

    httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), any(), anyInt()))
        .thenThrow(new RuntimeException("Invalid refresh token"));

    assertThrows(CustomException.class, () -> {
      salesforceTokens.revokeTokens(instanceUrl, refreshToken);
    });
    httpClientMockedStatic.close();
  }

}
