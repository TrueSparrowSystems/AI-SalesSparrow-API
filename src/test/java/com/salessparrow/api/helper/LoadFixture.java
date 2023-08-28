package com.salessparrow.api.helper;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;
import com.salessparrow.api.repositories.SalesforceOrganizationRepository;
import com.salessparrow.api.repositories.SalesforceUserRepository;

/**
 * LoadFixture is a helper class for the tests.
 */
public class LoadFixture {
  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private SalesforceUserRepository salesforceUserRepository;

  @Autowired
  private SalesforceOauthTokenRepository salesforceOauthTokenRepository;

  @Autowired
  private SalesforceOrganizationRepository salesforceOrganizationRepository;

  @Autowired
  private Util util;

  /**
   * Load the fixture data.
   * 
   * @param location
   * @return FixtureData
   * @throws IOException
   */
  public void perform(FixtureData fixtureData) throws IOException {

    if (fixtureData.getSalesforce_users() != null) {
      for (FilePathData filePathData : fixtureData.getSalesforce_users()) {
        SalesforceUser salesforceUser = loadSalesForceUserFixture(filePathData.getFilepath());
        salesforceUser.setCreatedAt(util.getCurrentTimeInDateFormat());
        salesforceUserRepository.saveSalesforceUser(salesforceUser);
      }
    }

    if (fixtureData.getSalesforce_oauth_tokens() != null) {
      for (FilePathData filePathData : fixtureData.getSalesforce_oauth_tokens()) {
        SalesforceOauthToken salesforceOauth = loadSalesForceOAuthTokenFixture(filePathData.getFilepath());
        salesforceOauth.setCreatedAt(util.getCurrentTimeInDateFormat());
        salesforceOauthTokenRepository.saveSalesforceOauthToken(salesforceOauth);
      }
    }

    if (fixtureData.getSalesforce_organizations() != null) {
      for (FilePathData filePathData : fixtureData.getSalesforce_organizations()) {
        SalesforceOrganization salesforceOrganization = loadSalesForceOrganizationFixture(filePathData.getFilepath());
        salesforceOrganization.setCreatedAt(util.getCurrentTimeInDateFormat());
        salesforceOrganizationRepository.saveSalesforceOrganization(salesforceOrganization);
      }
    }

  }

  /**
   * Load the SalesforceUser fixture data from the given location.
   * 
   * @param location
   * @return FixtureData
   * @throws IOException
   */
  public SalesforceUser loadSalesForceUserFixture(String location) throws IOException {
    Resource resource = resourceLoader.getResource(location);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceUser>() {
    });
  }

  /**
   * Load the SalesforceOauthToken fixture data from the given location.
   * 
   * @param location
   * @return FixtureData
   * @throws IOException
   */
  public SalesforceOauthToken loadSalesForceOAuthTokenFixture(String location) throws IOException {
    Resource resource = resourceLoader.getResource(location);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceOauthToken>() {
    });
  }

  /**
   * Load the SalesforceOrganization fixture data from the given location.
   * 
   * @param location
   * @return FixtureData
   * @throws IOException
   */
  public SalesforceOrganization loadSalesForceOrganizationFixture(String location) throws IOException {
    Resource resource = resourceLoader.getResource(location);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceOrganization>() {
    });
  }
}
