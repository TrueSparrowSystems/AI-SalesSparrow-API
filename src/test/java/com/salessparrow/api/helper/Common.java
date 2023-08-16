package com.salessparrow.api.helper;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.SalesforceUser;

/**
 * Common is a helper class for the tests.
 */
@TestConfiguration
public class Common {
  @Autowired
  private ResourceLoader resourceLoader;

  /**
   * Load the salesforce user fixture.
   * 
   * @param location
   * @return SalesforceUser
   * @throws IOException
   */
  public SalesforceUser loadSalesforceUserFixture(String location) throws IOException {
    Resource resource = resourceLoader.getResource(location);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(resource.getInputStream(), new TypeReference<SalesforceUser>() {});
  }
}
