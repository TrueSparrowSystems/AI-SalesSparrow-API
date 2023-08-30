package com.salessparrow.api.functional.controllers.accountController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.dto.formatter.PaginationIdentifierFormatterDto;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.Constants;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequestInterface;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class GetAccountsFeedTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Common common;
  @Autowired
  private LoadFixture loadFixture;
  @Autowired
  private Setup setup;
  @Autowired
  private Cleanup cleanup;

  @MockBean
  private MakeCompositeRequest makeCompositeRequestMock;

  @MockBean
  private SalesforceRequestInterface<HttpClient.HttpResponse> salesforceRequestInterfaceMock;

  @BeforeEach
  public void setUp() throws DynamobeeException {
    setup.perform();
  }

  @AfterEach
  public void tearDown() {
    cleanup.perform();
  }

  @ParameterizedTest
  @MethodSource("testScenariosProvider")
  public void getAccountsFeed(Scenario testScenario) throws Exception {
    System.out.println("Description: " + testScenario.getDescription());

    String currentFunctionName = new Object() {
    }.getClass().getEnclosingMethod().getName();
    // Load fixture data and set up mocks
    loadFixtureDataAndSetUpMocks(testScenario, currentFunctionName);

    // Perform the request
    ResultActions resultActions = performRequest(testScenario);

    // Check the response
    checkResponse(testScenario, resultActions);
  }

  private void loadFixtureDataAndSetUpMocks(Scenario testScenario, String currentFunctionName) throws Exception {

    FixtureData fixtureData = common.loadFixture(
        "classpath:fixtures/controllers/accountController/getAccountsFeed.fixtures.json", currentFunctionName);
    loadFixture.perform(fixtureData);

    // Read data from the scenario
    ObjectMapper objectMapper = new ObjectMapper();

    // Prepare makeCompositeRequestMock responses
    if (testScenario.getMocks() != null && testScenario.getMocks().containsKey("makeCompositeRequest")) {
      HttpResponse getAccountMockResponse = new HttpResponse();
      getAccountMockResponse
          .setResponseBody(objectMapper.writeValueAsString(testScenario.getMocks().get("makeCompositeRequest")));
      when(makeCompositeRequestMock.makePostRequest(any(), any())).thenReturn(getAccountMockResponse);
    }

    if (testScenario.getMocks() != null && testScenario.getMocks().containsKey("writeValueAsString")) {
      ObjectMapper mapper = mock(ObjectMapper.class);

      String errorMessage = testScenario.getMocks().get("writeValueAsString").toString();
      when(mapper.writeValueAsString(any()))
          .thenThrow(new RuntimeException(errorMessage));
    }

    if (testScenario.getMocks() != null && testScenario.getMocks().containsKey("readValue")) {
      ObjectMapper mapper = mock(ObjectMapper.class);

      System.out.println("in base64Encode");
      String errorMessage = testScenario.getMocks().get("readValue").toString();
      when(mapper.readValue(any(JsonParser.class), eq(PaginationIdentifierFormatterDto.class)))
          .thenThrow(new RuntimeException(errorMessage));
    }

    // Prepare salesforceRequestInterfaceMock responses
    if (testScenario.getMocks() != null && testScenario.getMocks().containsKey("SalesforceRequestInterfaceError")) {
      String errorMessage = testScenario.getMocks().get("SalesforceRequestInterfaceError").toString();

      when(salesforceRequestInterfaceMock.execute(any(), any()))
          .thenThrow(new RuntimeException(errorMessage));
    }
  }

  private ResultActions performRequest(Scenario testScenario) throws Exception {
    // existing code for performing the request

    String url = "/api/v1/accounts/feed";
    String cookieValue = Constants.SALESFORCE_ACTIVE_USER_COOKIE_VALUE;
    String paginationIdentifier = testScenario.getInput().get("pagination_identifier").toString();

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
        .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
        .param("pagination_identifier", paginationIdentifier)
        .contentType(MediaType.APPLICATION_JSON));

    return resultActions;
  }

  private void checkResponse(Scenario testScenario, ResultActions resultActions) throws Exception {
    // existing code for checking the response
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedOutput = objectMapper.writeValueAsString(testScenario.getOutput());
    String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

    JsonNode expectedOutputJson = objectMapper.readTree(expectedOutput);
    JsonNode actualOutputJson = objectMapper.readTree(actualOutput);

    if (resultActions.andReturn().getResponse().getStatus() == 200) {
      assertEquals(expectedOutputJson, actualOutputJson);
    } else {
      common.compareErrors(testScenario, actualOutput);
    }
  }

  static Stream<Scenario> testScenariosProvider() throws IOException {
    List<Scenario> testScenarios = loadScenarios();
    return testScenarios.stream();
  }

  private static List<Scenario> loadScenarios() throws IOException {
    String scenariosPath = "classpath:data/functional/controllers/accountController/getAccountsFeed.scenarios.json";
    Resource resource = new DefaultResourceLoader().getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {
    });
  }
}
