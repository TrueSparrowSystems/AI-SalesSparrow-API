package com.salessparrow.api.unit.lib.openAi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.openAi.OpenAiRequest;

@SpringBootTest
@Import({Common.class, OpenAiRequest.class})
public class OpenAiRequestTest {
  @Autowired
  private OpenAiRequest openAiRequest;

  @Autowired
  private Common common;

  @MockBean
  private HttpClient httpClientMock;

  @Test
  void testOpenAiRequest() throws IOException {
    List<Scenario> testDataItems = common.loadScenariosData("classpath:data/unit/lib/openAi/openAiRequest.scenarios.json");
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    for (Scenario testDataItem : testDataItems) {
      ObjectMapper objectMapper = new ObjectMapper();

      HttpResponse openAiMocksResponse = new HttpResponse();
      openAiMocksResponse.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("makeRequest")));
      httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), any(), anyInt()))
          .thenReturn(openAiMocksResponse);

      HttpResponse actualResponse = openAiRequest.makeRequest(testDataItem.getInput().get("payload"));
      String expectedOutput = objectMapper.writeValueAsString(testDataItem.getOutput());

      assertEquals(actualResponse.getResponseBody(), expectedOutput);
    }
    httpClientMockedStatic.close();
  }

  @Test
  void testOpenAiRequestError() throws IOException {
    List<Scenario> testDataItems = common.loadScenariosData("classpath:data/unit/lib/openAi/openAiRequestError.scenarios.json");
    MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

    for (Scenario testDataItem : testDataItems) {
      int statusCode = (int) testDataItem.getInput().get("statusCode");
      httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), any(), anyInt()))
          .thenThrow( new WebClientResponseException("error", statusCode, "error", null, null, null)); 
      try {
        openAiRequest.makeRequest(testDataItem.getInput().get("payload"));
      } catch (CustomException e) {
        String expectedApiErrorIdentifier = (String) testDataItem.getOutput().get("apiErrorIdentifier");

        assertEquals(e.getErrorObject().getApiErrorIdentifier(), expectedApiErrorIdentifier);
      }
    }
    httpClientMockedStatic.close();
  }
}
