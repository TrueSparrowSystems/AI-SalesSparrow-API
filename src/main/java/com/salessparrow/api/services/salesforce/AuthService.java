package com.salessparrow.api.services.salesforce;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.SalesforceConnectDto;
import com.salessparrow.api.dto.formatter.CurrentUserEntityDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.LocalCipher;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.globalConstants.UserConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;
import com.salessparrow.api.repositories.SalesforceOrganizationRepository;
import com.salessparrow.api.repositories.SalesforceUserRepository;

@Service
public class AuthService {

  private String code;
  private String redirectUri;

  private JsonNode tokenData;
  private JsonNode userData;
  private SalesforceOauthToken salesforceOauthToken;
  private SalesforceUser salesforceUser;
  private String decryptedSalt;
  private Boolean isNewUser = true;

  Logger logger = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  private CoreConstants coreConstants;
  @Autowired
  private AwsKms awsKms;
  @Autowired
  private Util util;
  @Autowired
  private SalesforceOauthTokenRepository salesforceOauthTokenRepository;
  @Autowired
  private SalesforceUserRepository salesforceUserRepository;
  @Autowired
  private SalesforceOrganizationRepository salesforceOrganizationRepository;
  @Autowired
  private LocalCipher localCipher;
  @Autowired
  private CookieHelper cookieHelper;
  @Autowired
  private SalesforceConstants salesforceConstants;

  /**
   * Connect to Salesforce and create user if not exists.
   * 
   * @param params
   * 
   * @return AuthServiceDto
   */
  public AuthServiceDto connectToSalesforce(SalesforceConnectDto params) {
    code = params.getCode();
    redirectUri = params.getRedirect_uri();

    // Call Salesforce OAuth Token API
    fetchOauthTokensFromSalesforce();

    // Save Salesforce OAuth Tokens in DB
    saveSalesforceOAuthTokens();

    if (this.isNewUser) {
      // Call Salesforce Identity API
      fetchUserInfoFromSalesforce();

      // Save Salesforce User in DB
      createSalesforceUser();
    }

    // Create Salesforce Organization if not exists
    createSalesforceOrganizationIfNotExists();

    return prepareResponse();
  }

  /**
   * Calls the Salesforce OAuth Token API.
   * 
   * @return void
   */
  private void fetchOauthTokensFromSalesforce() {

    String salesforceOAuthEndpoint = salesforceConstants.oauth2Url();

    String requestBody = "grant_type=" + salesforceConstants.authorizationCodeGrantType() + "&client_id="
        + coreConstants.salesforceClientId()
        + "&client_secret="
        + coreConstants.salesforceClientSecret() +
        "&code=" + this.code + "&redirect_uri=" + this.redirectUri;

    Map<String, String> headers = new HashMap<>();
    headers.put("content-type", "application/x-www-form-urlencoded");

    HttpResponse response = null;
    try {
      response = HttpClient.makePostRequest(
          salesforceOAuthEndpoint,
          headers,
          requestBody,
          10000);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "s_s_as_csota_1",
              "bad_request",
              e.getMessage()));
    }

    JsonNode jsonNode = util.getJsonNode(response.getResponseBody());
    this.tokenData = jsonNode;
  }

  /**
   * Save Salesforce OAuth Tokens in DB.
   * 
   * @return void
   */
  private void saveSalesforceOAuthTokens() {

    String encryptedAccessToken = awsKms.encryptToken(this.tokenData.path("access_token").asText());
    String encryptedRefreshToken = awsKms.encryptToken(this.tokenData.path("refresh_token").asText());
    String salesforceUserId = this.tokenData.path("id").asText().split("/")[5];

    SalesforceOauthToken salesforceOauthToken = new SalesforceOauthToken();
    salesforceOauthToken.setIdentityUrl(this.tokenData.path("id").asText());
    salesforceOauthToken.setAccessToken(encryptedAccessToken);
    salesforceOauthToken.setRefreshToken(encryptedRefreshToken);
    salesforceOauthToken.setSignature(this.tokenData.path("signature").asText());
    salesforceOauthToken.setIdToken(this.tokenData.path("id_token").asText());
    salesforceOauthToken.setInstanceUrl(this.tokenData.path("instance_url").asText());
    salesforceOauthToken.setStatus(SalesforceOauthToken.Status.ACTIVE);
    salesforceOauthToken.setIssuedAt(Long.parseLong(this.tokenData.path("issued_at").asText()));
    salesforceOauthToken.setSalesforceUserId(salesforceUserId);

    SalesforceUser salesforceUser = salesforceUserRepository.getSalesforceUserByExternalUserId(salesforceUserId);

    if (salesforceUser != null) {
      logger.info("Salesforce User already exists");
      this.salesforceUser = salesforceUser;
      this.isNewUser = false;

      this.decryptedSalt = localCipher.decrypt(coreConstants.encryptionKey(), salesforceUser.getEncryptionSalt());

      SalesforceOauthToken exsitingOauthTokenData = salesforceOauthTokenRepository
          .getSalesforceOauthTokenBySalesforceUserId(salesforceUserId);
      salesforceOauthToken.setId(exsitingOauthTokenData.getId());
      logger.info("Salesforce OAuth Token already exists");
    }

    this.salesforceOauthToken = salesforceOauthTokenRepository
        .upsertSalesforceOauthToken(salesforceOauthToken);

    if (this.salesforceOauthToken == null) {
      throw new CustomException(
          new ErrorObject(
              "s_s_as_csot_1",
              "internal_server_error",
              "Error while saving Salesforce OAuth Token in DB"));
    }
  }

  /**
   * Fetch user info from Salesforce Identity API.
   * 
   * @return void
   */
  private void fetchUserInfoFromSalesforce() {

    logger.info("Calling Salesforce Identity API");
    String salesforceIdentityEndpoint = salesforceOauthToken.getInstanceUrl() + salesforceConstants.identityUrl();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + this.tokenData.path("access_token").asText());

    HttpResponse response = HttpClient.makeGetRequest(
        salesforceIdentityEndpoint,
        headers,
        10000);

    JsonNode jsonNode = util.getJsonNode(response.getResponseBody());

    if (jsonNode == null) {
      throw new CustomException(
          new ErrorObject(
              "s_s_as_csia_1",
              "internal_server_error",
              "Error while calling Salesforce Identity API"));
    }

    this.userData = jsonNode;
  }

  /**
   * Create Salesforce User in DB.
   * 
   * @return void
   */
  private void createSalesforceUser() {
    logger.info("Creating Salesforce User");

    String decryptedSalt = localCipher.generateRandomSalt();
    String cookieToken = localCipher.generateRandomIv(32);

    String encryptedSalt = localCipher.encrypt(coreConstants.encryptionKey(), decryptedSalt);
    String encryptedCookieToken = localCipher.encrypt(decryptedSalt, cookieToken);

    SalesforceUser salesforceUser = new SalesforceUser();
    salesforceUser.setExternalUserId(this.userData.path("user_id").asText());
    salesforceUser.setIdentityUrl(this.userData.path("sub").asText());
    salesforceUser.setSalesforceOrganizationId(this.userData.path("organization_id").asText());
    salesforceUser.setName(this.userData.path("name").asText());
    salesforceUser.setEmail(this.userData.path("email").asText());
    salesforceUser.setUserType(this.userData.path("user_type").asText());
    salesforceUser.setCookieToken(encryptedCookieToken);
    salesforceUser.setEncryptionSalt(encryptedSalt);
    salesforceUser.setStatus(SalesforceUser.Status.ACTIVE);

    this.salesforceUser = salesforceUserRepository.saveSalesforceUser(salesforceUser);
    this.decryptedSalt = decryptedSalt;

    if (this.salesforceUser == null) {
      throw new CustomException(
          new ErrorObject(
              "s_s_as_csu_1",
              "internal_server_error",
              "Error while saving Salesforce User in DB"));
    }
  }

  /**
   * Create Salesforce Organization if not exists.
   * 
   * @return void
   */
  private void createSalesforceOrganizationIfNotExists() {

    SalesforceOrganization existingOrganizationData = salesforceOrganizationRepository
        .getSalesforceOrganizationByExternalOrganizationId(this.salesforceUser.getSalesforceOrganizationId());

    if (existingOrganizationData != null) {
      logger.info("Salesforce Organization already exists");
      return;
    }

    SalesforceOrganization salesforceOrganization = new SalesforceOrganization();
    salesforceOrganization.setExternalOrganizationId(this.salesforceUser.getSalesforceOrganizationId());
    salesforceOrganization.setStatus(SalesforceOrganization.Status.ACTIVE);

    logger.info("Creating Salesforce Organization");
    SalesforceOrganization salesforceOrganizationData = salesforceOrganizationRepository
        .saveSalesforceOrganization(salesforceOrganization);

    if (salesforceOrganizationData == null) {
      throw new CustomException(
          new ErrorObject(
              "s_s_as_cso_1",
              "internal_server_error",
              "Error while saving Salesforce Organization in DB"));
    }
  }

  /**
   * Prepare service response.
   * 
   * @return AuthServiceDto
   */
  private AuthServiceDto prepareResponse() {
    logger.info("Preparing response");
    AuthServiceDto authServiceDto = new AuthServiceDto();

    CurrentUserEntityDto currentUserEntityDto = new CurrentUserEntityDto();
    currentUserEntityDto.setId(this.salesforceUser.getId());
    currentUserEntityDto.setName(this.salesforceUser.getName());
    currentUserEntityDto.setEmail(this.salesforceUser.getEmail());

    String userLoginCookieValue = cookieHelper.getCookieValue(this.salesforceUser,
        UserConstants.SALESFORCE_USER_KIND,
        this.decryptedSalt,
        System.currentTimeMillis());

    authServiceDto.setCurrentUser(currentUserEntityDto);
    authServiceDto.setCurrentUserLoginCookie(userLoginCookieValue);
    return authServiceDto;
  }

  /**
   * DTO for AuthService.
   * 
   * @return AuthServiceDto
   */
  public class AuthServiceDto {

    private CurrentUserEntityDto currentUser;
    private String currentUserLoginCookie;

    public CurrentUserEntityDto getCurrentUser() {
      return currentUser;
    }

    public void setCurrentUser(CurrentUserEntityDto currentUser) {
      this.currentUser = currentUser;
    }

    public String getCurrentUserLoginCookie() {
      return currentUserLoginCookie;
    }

    public void setCurrentUserLoginCookie(String currentUserLoginCookie) {
      this.currentUserLoginCookie = currentUserLoginCookie;
    }

  }
}