package com.salessparrow.api.functional.controllers.accountController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
public class CreateNoteTest {

  Logger logger = LoggerFactory.getLogger(CreateNoteTest.class);
  
  @Autowired
  private ResourceLoader resourceLoader;
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

  @Test
  public void createNote() throws Exception{
    String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
    FixtureData fixtureData = common.loadFixture(
      "classpath:fixtures/controllers/accountController/createNote.fixtures.json",
      currentFunctionName
    );
    loadFixture.perform(fixtureData);

    List<Scenario> testScenarios = loadScenarios();

    for (Scenario testScenario: testScenarios) {
      ObjectMapper objectMapper = new ObjectMapper();

      String cookieValue = (String) testScenario.getInput().get("cookie");
      String accountId = (String) testScenario.getInput().get("accountId");

      HttpResponse createNoteMockResponse = new HttpResponse();
      createNoteMockResponse.setResponseBody(objectMapper.writeValueAsString(testScenario.getMocks().get("makeCompositeRequest")));

      when(makeCompositeRequestMock.makePostRequest(any(), any())).thenReturn(createNoteMockResponse);

      String requestBody = objectMapper.writeValueAsString(testScenario.getInput().get("body"));
      String url = "/api/v1/accounts/" + accountId + "/notes";
      ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
        .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON));

      String expectedOutput = objectMapper.writeValueAsString(testScenario.getOutput());

      
      String actualOutput = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(expectedOutput, actualOutput);
    }
  }

  public List<Scenario> loadScenarios() throws IOException {
    String scenariosPath = "classpath:data/controllers/accountController/createNote.scenarios.json";
    Resource resource = resourceLoader.getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {});
  }
}
