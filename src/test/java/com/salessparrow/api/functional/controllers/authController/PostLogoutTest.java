package com.salessparrow.api.functional.controllers.authController;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
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
        "classpath:fixtures/functional/controllers/authController/PostLogoutFixture.json",
        currentFunctionName);
    loadFixture.perform(fixtureData);

    List<Scenario> testDataItems = common.loadScenariosData(
        "classpath:data/functional/controllers/authController/Logout.scenarios.json", currentFunctionName);

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
}
