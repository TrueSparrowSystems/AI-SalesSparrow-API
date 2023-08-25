package com.salessparrow.api.lib.globalConstants;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.amazonaws.secretsmanager.caching.SecretCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClientBuilder;

/**
 * Class to get the environment variables from aws secret manager.
 */
public class SecretConstants {

  /* Secret manager configuration start */

  /**
   * This is the builder that is going to be used to access the secrets manager.
   */
  // public static SecretsManagerClientBuilder secretsManagerClientBuilder = SecretsManagerClient.builder()
  //         .credentialsProvider(InstanceProfileCredentialsProvider.builder().build());
  
  public static SecretsManagerClientBuilder secretsManagerClientBuilder = SecretsManagerClient.builder();

  /**
   * This method returns the secrets from the secrets manager.
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public static String getSecret(String key) {
    String secretJson = "";

    if (CoreConstants.isDevEnvironment()) {
      secretJson = getLocalEnvVars("secrets.json");
    } else if (CoreConstants.isLocalTestEnvironment()) {
      secretJson = getLocalEnvVars("test.secrets.json");
    } else {
      SecretCache cache = new SecretCache(secretsManagerClientBuilder);
      String secretId = getSecretId();
      secretJson = cache.getSecretString(secretId);
      cache.close();
    }

    System.out.println("================================secretJson: " + secretJson);
    ObjectMapper objectMapper = new ObjectMapper();
    String specificValue = "";
    try {
      JsonNode jsonNode = objectMapper.readTree(secretJson);
      specificValue = jsonNode.get(key).asText();
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "l_gc_s_gs_1",
              "something_went_wrong",
              e.getMessage()));
    }

    return specificValue;
  }

  /**
   * This method returns the secret id that is going to be 
   * used to access the secrets manager.
   * 
   * @return
   */
  private static String getSecretId() {
    return "ai-sales-sparrow-api-" + CoreConstants.environment();
  }

  /* Secret manager configuration function end */

  /* Secrets start */

  public static String cookieDomain() {
    return getSecret("COOKIE_DOMAIN");
  }

  public static String awsRegion() {
    return getSecret("AWS_IAM_REGION");
  }

  public static String encryptionKey() {
    return getSecret("ENCRYPTION_KEY");
  }

  public static String apiCookieSecret() {
    return getSecret("API_COOKIE_SECRET");
  }

  public static String kmsKeyId() {
    return getSecret("KMS_KEY_ID");
  }

  public static String salesforceAuthUrl() {
    return getSecret("SALESFORCE_AUTH_URL");
  }

  public static String salesforceClientId() {
    return getSecret("SALESFORCE_CLIENT_ID");
  }

  public static String salesforceClientSecret() {
    return getSecret("SALESFORCE_CLIENT_SECRET");
  }

  public static String memcachedHost() {
    return getSecret("MEMCACHED_CACHE_HOST");
  }

  public static String memcachedPort() {
    return getSecret("MEMCACHED_CACHE_PORT");
  }

  public static String salesforceWhitelistedRedirectUris() {
    return getSecret("SALESFORCE_WHITELISTED_REDIRECT_URIS");
  }

  public static String errorEmailFrom() {
    return getSecret("ERROR_EMAIL_FROM");
  }

  public static String errorEmailTo() {
    return getSecret("ERROR_EMAIL_TO");
  }

  public static String dynamoDbUrl() {
    return getSecret("DYNAMO_DB_URL");
  }

  public static String localKmsEndpoint() {
    return getSecret("LOCAL_KMS_ENDPOINT");
  }

  /* Secrets end */

  /**
   * This method returns the local environment variables.
   * 
   * @return String
   */
  private static String getLocalEnvVars(String filename) {
    try (FileReader fileReader = new FileReader(filename)) {
      int ch;
      StringBuilder secretJsonBuilder = new StringBuilder();

      while ((ch = fileReader.read()) != -1) {
          secretJsonBuilder.append((char) ch);
      }

      return secretJsonBuilder.toString();
    } catch (FileNotFoundException e) {
      throw new CustomException(
          new ErrorObject(
            "l_gc_s_glev_1", 
            "something_went_wrong", 
            e.getMessage()));
    } catch (IOException e) {
      throw new CustomException(
          new ErrorObject(
            "l_gc_s_glev_2", 
            "something_went_wrong", 
            e.getMessage()));
    }
  }
}
