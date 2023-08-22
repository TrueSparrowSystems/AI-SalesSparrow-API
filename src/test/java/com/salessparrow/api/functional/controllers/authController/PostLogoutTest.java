package com.salessparrow.api.functional.controllers.authController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.github.dynamobee.exception.DynamobeeException;
import com.google.common.net.HttpHeaders;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.globalConstants.CookieConstants;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class PostLogoutTest {
  @Autowired
  private ResourceLoader resourceLoader;

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

  @BeforeEach
  public void setUp() throws DynamobeeException, IOException {
    setup.perform();
  }

  @AfterEach
  public void tearDown() {
    cleanup.perform();
  }

  @Test
  public void testPostLogout() throws Exception {
    String currentFunctionName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    FixtureData fixtureData = common.loadFixture(
        "classpath:fixtures/controllers/authController/PostLogoutFixture.json",
        currentFunctionName);
    loadFixture.perform(fixtureData);

    List<Scenario> testDataItems = loadTestData(currentFunctionName);

    for (Scenario testDataItem : testDataItems) {
      System.out.println("Test description: " + testDataItem.getDescription());
      ObjectMapper objectMapper = new ObjectMapper();
      String expectedOutput = objectMapper.writeValueAsString(testDataItem.getOutput());
      String cookieValue = (String) testDataItem.getInput().get("cookie");

      ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")
          .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
          .contentType(MediaType.APPLICATION_JSON));

      String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

      if (resultActions.andReturn().getResponse().getStatus() != 200) {
        System.out.println("Expected output: " + expectedOutput);
        System.out.println("Actual output: " + actualOutput);
        System.out.println("Status code: " + resultActions.andReturn().getResponse().getStatus());
        common.compareErrors(testDataItem, actualOutput);
      }
    }
  }

  public List<Scenario> loadTestData(String key) throws IOException {
    String scenariosPath = "classpath:data/controllers/authController/Logout.scenarios.json";
    Resource resource = resourceLoader.getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, List<Scenario>> scenariosMap = new HashMap<>();
    scenariosMap = objectMapper.readValue(resource.getInputStream(),
        new TypeReference<HashMap<String, List<Scenario>>>() {
        });
    return scenariosMap.get(key);
  }
}