package com.salessparrow.api.functional.controllers.authController;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class })
public class GetRedirectUrlTest {
  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Setup setup;

  @Autowired
  private Cleanup cleanup;
  
  @BeforeEach
  public void setUp() {
    setup.perform();
  }

  @AfterEach
  public void tearDown() {
    cleanup.perform();
  }

  @Test
  public void testGetRedirectUrl() throws Exception{
    List<Scenario> testDataItems = loadTestData();

    for (Scenario testDataItem : testDataItems) {
      ObjectMapper objectMapper = new ObjectMapper();
      String expectedOutput = objectMapper.writeValueAsString(testDataItem.getOutput());

      ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/salesforce/redirect-url")
        .param("redirect_uri", (String) testDataItem.getInput().get("redirect_uri"))
        .param("state", (String) testDataItem.getInput().get("state"))
        .contentType(MediaType.APPLICATION_JSON));

      if(resultActions.andReturn().getResponse().getStatus() == 200) {
        String actualOutput = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(expectedOutput, actualOutput);
      } else {
        assertEquals(testDataItem.getOutput().get("status"), resultActions.andReturn().getResponse().getStatus());
      }
    }
  }

  public List<Scenario> loadTestData() throws IOException {
    String scenariosPath = "classpath:data/controllers/authController/redirectUrl.scenarios.json";
    Resource resource = resourceLoader.getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {});
  }
}
