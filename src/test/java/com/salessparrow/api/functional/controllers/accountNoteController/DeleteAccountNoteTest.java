package com.salessparrow.api.functional.controllers.accountNoteController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.Constants;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class DeleteAccountNoteTest {

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
  public void deleteAccountNote(Scenario testScenario) throws Exception {

    // Load fixture data
    String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
    FixtureData fixtureData = common.loadFixture("classpath:fixtures/controllers/accountNoteController/deleteAccountNote.fixtures.json", currentFunctionName);
    loadFixture.perform(fixtureData);

    // Read data from the scenario
    ObjectMapper objectMapper = new ObjectMapper();
    String cookieValue = Constants.SALESFORCE_ACTIVE_USET_COOKIE_VALUE;
    String accountId = (String) testScenario.getInput().get("accountId");
    String noteId = (String) testScenario.getInput().get("noteId");

    // Prepare mock responses
    HttpResponse deleteNoteMockResponse = new HttpResponse();
    deleteNoteMockResponse.setResponseBody(objectMapper.writeValueAsString(testScenario.getMocks().get("makeCompositeRequest")));
    when(makeCompositeRequestMock.makePostRequest(any(), any())).thenReturn(deleteNoteMockResponse);

    // Perform the request
    String url = "/api/v1/accounts/" + accountId + "/notes/" + noteId ;

    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url)
            .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
            .contentType(MediaType.APPLICATION_JSON));

    // Check the response
    String expectedOutput = objectMapper.writeValueAsString(testScenario.getOutput());
    String actualOutput = resultActions.andReturn().getResponse().getContentAsString();
    assertCustomEquals(expectedOutput, actualOutput);
  }

  static Stream<Scenario> testScenariosProvider() throws IOException {
    List<Scenario> testScenarios = loadScenarios();
    return testScenarios.stream();
  }

  private static List<Scenario> loadScenarios() throws IOException {
    String scenariosPath = "classpath:data/controllers/accountNoteController/deleteAccountNote.scenarios.json";
    Resource resource = new DefaultResourceLoader().getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {});
  }

  // Custom assertion method that treats <null> and <> as the same
  public static void assertCustomEquals(String expected, String actual) {

    if (expected.equals("{}")) {
      expected = "";
    }

    assertEquals(expected, actual);
  }
}
