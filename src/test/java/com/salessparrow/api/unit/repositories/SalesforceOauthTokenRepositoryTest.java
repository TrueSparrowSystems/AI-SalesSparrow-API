package com.salessparrow.api.unit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

@SpringBootTest
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class SalesforceOauthTokenRepositoryTest {
  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private Setup setup;

  @Autowired
  private Cleanup cleanup;

  @Autowired
  private Common common;

  @Autowired
  private LoadFixture loadFixture;

  @Autowired
  private SalesforceOauthTokenRepository sfOauthTokenRepository;

  @Autowired
  private Util util;

  @BeforeEach
  public void setUp() throws DynamobeeException, IOException {
    setup.perform();
  }

  @AfterEach
  public void tearDown() {
    cleanup.perform();
  }

  @Test
  public void testInsert() throws Exception {
    String currentFunctionName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    List<Scenario> testDataItems = loadTestData(currentFunctionName);

    for (Scenario testDataItem : testDataItems) {
      System.out.println("Test description: " + testDataItem.getDescription());

      ObjectMapper objectMapper = new ObjectMapper();
      SalesforceOauthToken salesforceOauthToken = objectMapper.readValue(
          objectMapper.writeValueAsString(testDataItem.getInput()),
          new TypeReference<SalesforceOauthToken>() {
          });
      salesforceOauthToken.setCreatedAt(util.getCurrentTimeInDateFormat());

      SalesforceOauthToken insertedSalesforceOauthToken = sfOauthTokenRepository
          .saveSalesforceOauthToken(salesforceOauthToken);

      assertNotNull(insertedSalesforceOauthToken);
      assertNotNull(insertedSalesforceOauthToken.getExternalUserId());
      assertNotNull(insertedSalesforceOauthToken.getCreatedAt());
      assertNotNull(insertedSalesforceOauthToken.getUpdatedAt());
    }
  }

  @Test
  public void testUpdate() throws Exception {
    String currentFunctionName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    FixtureData fixtureData = common.loadFixture(
        "classpath:fixtures/repositories/salesforceOauthTokenRepository.fixture.json",
        currentFunctionName);
    loadFixture.perform(fixtureData);

    List<Scenario> testDataItems = loadTestData(currentFunctionName);

    for (Scenario testDataItem : testDataItems) {
      System.out.println("Test description: " + testDataItem.getDescription());

      ObjectMapper objectMapper = new ObjectMapper();
      SalesforceOauthToken salesforceOauthToken = objectMapper.readValue(
          objectMapper.writeValueAsString(testDataItem.getInput()),
          new TypeReference<SalesforceOauthToken>() {
          });

      SalesforceOauthToken existingSalesforceOauthToken = sfOauthTokenRepository
          .getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

      System.out.println("existingSalesforceOauthToken: " + existingSalesforceOauthToken);

      sfOauthTokenRepository
          .saveSalesforceOauthToken(salesforceOauthToken);

      SalesforceOauthToken updatedSalesforceOauthToken = sfOauthTokenRepository
          .getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

      System.out.println("updatedSalesforceOauthToken: " + updatedSalesforceOauthToken);

      assertNotNull(updatedSalesforceOauthToken);
      assertNotNull(updatedSalesforceOauthToken.getUpdatedAt());
      assertEquals(updatedSalesforceOauthToken.getCreatedAt(), existingSalesforceOauthToken.getCreatedAt());
      assertEquals(updatedSalesforceOauthToken.getInstanceUrl(), existingSalesforceOauthToken.getInstanceUrl());
      assertEquals(updatedSalesforceOauthToken.getAccessToken(), salesforceOauthToken.getAccessToken());
      assertEquals(updatedSalesforceOauthToken.getRefreshToken(), salesforceOauthToken.getRefreshToken());
    }
  }

  @Test
  public void testUpdateWithNullAttributes() throws Exception {
    String currentFunctionName = new Object() {
    }.getClass().getEnclosingMethod().getName();

    FixtureData fixtureData = common.loadFixture(
        "classpath:fixtures/repositories/salesforceOauthTokenRepository.fixture.json",
        currentFunctionName);
    loadFixture.perform(fixtureData);

    System.out.println("Test description: " + "Update with null attributes");

    List<Scenario> testDataItems = loadTestData(currentFunctionName);

    System.out.println("Test description: " + "After Update with null attributes");

    for (Scenario testDataItem : testDataItems) {
      System.out.println("Test description: " + testDataItem.getDescription());

      ObjectMapper objectMapper = new ObjectMapper();
      SalesforceOauthToken salesforceOauthToken = objectMapper.readValue(
          objectMapper.writeValueAsString(testDataItem.getInput()),
          new TypeReference<SalesforceOauthToken>() {
          });

      SalesforceOauthToken existingSalesforceOauthToken = sfOauthTokenRepository
          .getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

      System.out.println("existingSalesforceOauthToken: " + existingSalesforceOauthToken);

      sfOauthTokenRepository
          .saveSalesforceOauthToken(salesforceOauthToken);

      SalesforceOauthToken updatedSalesforceOauthToken = sfOauthTokenRepository
          .getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());
      System.out.println("updatedSalesforceOauthToken: " + updatedSalesforceOauthToken);

      assertEquals(updatedSalesforceOauthToken.getAccessToken(), salesforceOauthToken.getAccessToken());
      assertEquals(updatedSalesforceOauthToken.getIdToken(), existingSalesforceOauthToken.getIdToken());
    }
  }

  public List<Scenario> loadTestData(String key) throws IOException {
    String scenariosPath = "classpath:data/repositories/salesforceOauthTokenRepository.scenarios.json";
    Resource resource = resourceLoader.getResource(scenariosPath);
    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, List<Scenario>> scenariosMap = new HashMap<>();
    scenariosMap = objectMapper.readValue(resource.getInputStream(),
        new TypeReference<HashMap<String, List<Scenario>>>() {
        });
    return scenariosMap.get(key);
  }
}
