package com.salessparrow.api.config;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Component
public class CoreConstants {
  @Autowired
  private Environment env;

  public String encryptionKey() {
    return env.getProperty("ENCRYPTION_KEY");
  }

  public String apiCookieSecret() {
    return env.getProperty("API_COOKIE_SECRET");
  }

  public String environment() {
    return env.getProperty("ENVIRONMENT");
  }

  public Boolean isDevEnvironment() {
    return environment().equals("development");
  }

  public String awsAccessKeyId() {
    return env.getProperty("AWS_ACCESS_KEY_ID");
  }

  public String awsSecretAccessKey() {
    return env.getProperty("AWS_SECRET_ACCESS_KEY");
  }

  public String awsRegion() {
    return env.getProperty("AWS_REGION");
  }

  public String cacheClusterId() {
    return env.getProperty("CACHE_CLUSTER_ID");
  }

  public String kmsKeyId() {
    return env.getProperty("KMS_KEY_ID");
  }

  public String salesforceAuthUrl() {
    return env.getProperty("SALESFORCE_AUTH_URL");
  }

  public String salesforceClientId() {
    return env.getProperty("SALESFORCE_CLIENT_ID");
  }

  public String salesforceClientSecret() {
    return env.getProperty("SALESFORCE_CLIENT_SECRET");
  }

  public String salesforceClientBaseUrl() {
    return env.getProperty("SALESFORCE_CLIENT_BASE_URL");
  }

  public String tableNamePrefix() {
    if (environment().equals("test")) {
      return "test_";
    } else if (environment().equals("development")) {
      return "dev_";
    } else if (environment().equals("staging")) {
      return "staging_";
    } else if (environment().equals("production")) {
      return "prod_";
    }

    return "";
  }
}
