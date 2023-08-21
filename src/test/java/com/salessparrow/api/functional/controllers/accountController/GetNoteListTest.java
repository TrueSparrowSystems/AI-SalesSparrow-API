package com.salessparrow.api.functional.controllers.accountController;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequest;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class GetNoteListTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Setup setup;

  @Autowired
  private Cleanup cleanup;

  @Autowired
  private Common common;

  @Autowired
  private LoadFixture loadFixture;

  @MockBean
  private MakeCompositeRequest makeCompositeRequestMock;

  @MockBean
  private SalesforceRequest salesforceOauthRequestMock;

  @BeforeEach
  public void setUp() throws DynamobeeException, IOException {
    setup.perform();
  }

  @AfterEach
  public void tearDown() {
    cleanup.perform();
  }

  @ParameterizedTest
  @MethodSource("testScenariosProvider")
  public void getNoteList(Scenario testScenario) throws Exception{
    String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
    FixtureData fixtureData = common.loadFixture(
      "classpath:fixtures/controllers/accountController/getNotesList.fixtures.json",
      currentFunctionName
    );
    loadFixture.perform(fixtureData);

    ObjectMapper objectMapper = new ObjectMapper();

    String cookieValue = (String) testScenario.getInput().get("cookie");
    String accountId = (String) testScenario.getInput().get("accountId");
    List<String> documentIds = (List<String>) testScenario.getInput().get("documentId");

    if(documentIds == null) {
      documentIds = new ArrayList<String>();
    }

    // Mocking the CompositeRequests of the salesforce oauth request
    SalesforceQueryBuilder salesforceLib = new SalesforceQueryBuilder();
    String documentIdsQuery = salesforceLib.getContentDocumentIdUrl(accountId);
    String documentIdsUrl = new SalesforceConstants().queryUrlPath() + documentIdsQuery;
    CompositeRequestDto documentIdsCompositeReq = new CompositeRequestDto("GET", documentIdsUrl, "GetContentDocumentId");
    List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
    compositeRequests.add(documentIdsCompositeReq);


    HttpResponse getNotesListIdMockResponse = new HttpResponse();
    getNotesListIdMockResponse.setResponseBody(objectMapper.writeValueAsString(testScenario.getMocks().get("makeCompositeRequest1")));

    when(makeCompositeRequestMock.makePostRequest(eq(compositeRequests), any())).thenReturn(getNotesListIdMockResponse);
    
    // Mocking the CompositeRequests of the salesforce notes list request
    String notesQuery = salesforceLib.getNoteListIdUrl(documentIds);
    String notesUrl = new SalesforceConstants().queryUrlPath() + notesQuery;
    CompositeRequestDto noteCompositeRequest = new CompositeRequestDto("GET", notesUrl, "GetNotesList");
    List<CompositeRequestDto> noteCompositeRequests = new ArrayList<CompositeRequestDto>();
    noteCompositeRequests.add(noteCompositeRequest);
    
    
    HttpResponse getNotesListMockResponse = new HttpResponse();
    getNotesListMockResponse.setResponseBody(objectMapper.writeValueAsString(testScenario.getMocks().get("makeCompositeRequest2")));
    
    when(makeCompositeRequestMock.makePostRequest(eq(noteCompositeRequests), any())).thenReturn(getNotesListMockResponse);

    String url = "/api/v1/accounts/" + accountId + "/notes";
    ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
      .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
      .contentType(MediaType.APPLICATION_JSON));

    String expectedOutput = objectMapper.writeValueAsString(testScenario.getOutput());
    
    String actualOutput = resultActions.andReturn().getResponse().getContentAsString();
      assertEquals(expectedOutput, actualOutput);
  }

  static Stream<Scenario> testScenariosProvider() throws IOException {
    List<Scenario> testScenarios = loadScenarios();
    return testScenarios.stream();
  }

  public static List<Scenario> loadScenarios() throws IOException {
    String scenariosPath = "classpath:data/controllers/accountController/getNotesList.scenarios.json";
    Resource resource = new DefaultResourceLoader().getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {});
  }
}
