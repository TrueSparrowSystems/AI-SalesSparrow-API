package com.salessparrow.api.config;

/**
 * Class to get the environment variables.
 */
public class CoreConstants {

  public static String encryptionKey() {
    return System.getenv("ENCRYPTION_KEY");
  }

  public static String apiCookieSecret() {
    return System.getenv("API_COOKIE_SECRET");
  }

  public static String environment() {
    return System.getenv("ENVIRONMENT");
  }

  public static Boolean isDevEnvironment() {
    return environment().equals("development");
  }

  public static String awsAccessKeyId() {
    return System.getenv("AWS_IAM_ACCESS_KEY_ID");
  }

  public static String awsSecretAccessKey() {
    return System.getenv("AWS_IAM_SECRET_ACCESS_KEY");
  }

  public static String awsRegion() {
    return System.getenv("AWS_IAM_REGION");
  }

  public static String cacheClusterId() {
    return System.getenv("AWS_IAM_CACHE_CLUSTER_ID");
  }

  public static String kmsKeyId() {
    return System.getenv("KMS_KEY_ID");
  }

  public static String salesforceAuthUrl() {
    return System.getenv("SALESFORCE_AUTH_URL");
  }

  public static String salesforceClientId() {
    return System.getenv("SALESFORCE_CLIENT_ID");
  }

  public static String salesforceClientSecret() {
    return System.getenv("SALESFORCE_CLIENT_SECRET");
  }

  public static String salesforceClientBaseUrl() {
    return System.getenv("SALESFORCE_CLIENT_BASE_URL");
  }

  public static String tableNamePrefix() {
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

  public static String[] getWhitelistedRedirectUris() {
    String redirectUrisJson = System.getenv("SALESFORCE_WHITELISTED_REDIRECT_URIS");
    return redirectUrisJson.split(",");
  }

  public static String memcachedAddress() {
    return System.getenv("LOCAL_CACHE_HOST") + ":" + System.getenv("LOCAL_CACHE_PORT") ;
  }
}
