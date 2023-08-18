package com.salessparrow.api.functional.controllers.authController;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetIdentity;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetTokens;
import com.salessparrow.api.services.salesforce.AuthService;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class })
public class PostSalesforceConnectTest {
  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Setup setup;

  @Autowired
  private Cleanup cleanup;

  @MockBean
  private SalesforceGetTokens mockGetTokens;

  @MockBean
  private SalesforceGetIdentity mockGetIdentity;

  @Mock
  private HttpResponse getTokensHttpMockResponse;

  @InjectMocks
  private AuthService authService;

  @BeforeEach
  public void setUp() throws DynamobeeException, IOException {
    MockitoAnnotations.openMocks(this);
    setup.perform();
  }

  @AfterEach
  public void tearDown() {
    cleanup.perform();
  }

  @Test
  public void testPostSalesforceConnect() throws Exception {
    List<Scenario> testDataItems = loadTestData();

    for (Scenario testDataItem : testDataItems) {
      System.out.println("Test description: " + testDataItem.getDescription());
      ObjectMapper objectMapper = new ObjectMapper();

      HttpResponse getTokensMockRes = new HttpResponse();
      getTokensMockRes.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("getTokens")));

      HttpResponse getIdentityMockRes = new HttpResponse();
      getIdentityMockRes.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("getIdentity")));

      when(mockGetTokens.getTokens(anyString(), anyString())).thenReturn(getTokensMockRes);
      when(mockGetIdentity.getUserIdentity(anyString(), anyString())).thenReturn(getIdentityMockRes);

      ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/salesforce/connect")
          .content(objectMapper.writeValueAsString(testDataItem.getInput().get("body")))
          .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
          .contentType(MediaType.APPLICATION_JSON));

      String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

      JsonNode expJsonNode = objectMapper.readTree(
          objectMapper.writeValueAsString(testDataItem.getOutput()));

      JsonNode resJsonNode = objectMapper.readTree(actualOutput);

      assertEquals(expJsonNode.get("status").asInt(), resultActions.andReturn().getResponse().getStatus());
      if (resultActions.andReturn().getResponse().getStatus() == 200) {
        assertEquals(objectMapper.writeValueAsString(testDataItem.getOutput().get("body")), actualOutput);
      } else if (resultActions.andReturn().getResponse().getStatus() == 400) {

        // Todo: Loop over param_errors and check if param_error_identifier is present
        // in body

        String paramError = resJsonNode.get("param_errors").get(0).get("param_error_identifier").asText();
        assertEquals(expJsonNode.get("body").get("param_error_identifiers").get(0).asText(), paramError);
      } else {
        assertEquals(testDataItem.getOutput().get("status"), resultActions.andReturn().getResponse().getStatus());
      }
    }
  }

  public List<Scenario> loadTestData() throws IOException {
    String scenariosPath = "classpath:data/controllers/authController/SalesforceConnect.scenarios.json";
    Resource resource = resourceLoader.getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {
    });
  }

}
