package com.salessparrow.api.functional.controllers.userController;

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
public class GetCurrentUserTest {
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
  public void testGetCurrentUser() throws Exception{
    FixtureData fixtureData = common.loadFixture("classpath:fixtures/controllers/userController/getCurrentUser.fixtures.json");
    loadFixture.perform(fixtureData);

    List<Scenario> testDataItems = loadTestData();

    for (Scenario testDataItem : testDataItems) {
      ObjectMapper objectMapper = new ObjectMapper();
      String expectedOutput = objectMapper.writeValueAsString(testDataItem.getOutput());
      String cookieValue = (String) testDataItem.getInput().get("cookie");

      ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/current")
        .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
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
    String scenariosPath = "classpath:data/controllers/userController/getCurrentUser.scenarios.json";
    Resource resource = resourceLoader.getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {});
  }
}
