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

  public String dbName() {
    return env.getProperty("SPRING_BOOT_BE_DB_NAME");
  }

  public String kmsKeyId() {
    return env.getProperty("KMS_KEY_ID");
  }

  public String salesforceLoginUrl() {
    return env.getProperty("SALESFORCE_LOGIN_URL");
  }

  public String salesforceClientId() {
    return env.getProperty("SALESFORCE_CLIENT_ID");
  }
}
