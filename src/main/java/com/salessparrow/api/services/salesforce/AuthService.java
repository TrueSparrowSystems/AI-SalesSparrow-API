package com.salessparrow.api.services.salesforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.entities.CurrentUserEntityDto;
import com.salessparrow.api.dto.requestMapper.SalesforceConnectDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.LocalCipher;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetIdentityDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetTokensDto;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetIdentity;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetTokens;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;
import com.salessparrow.api.repositories.SalesforceOrganizationRepository;
import com.salessparrow.api.repositories.SalesforceUserRepository;

@Service
public class AuthService {

  private String code;
  private String redirectUri;

  private SalesforceGetTokensDto tokensData;
  private SalesforceGetIdentityDto userData;
  private SalesforceOauthToken salesforceOauthToken;
  private SalesforceUser salesforceUser;
  private String decryptedSalt;
  private Boolean isNewUser = true;

  Logger logger = LoggerFactory.getLogger(AuthService.class);

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
  private SalesforceGetTokens salesforceGetTokens;

  @Autowired
  private SalesforceGetIdentity salesforceGetIdentity;

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

    fetchOauthTokensFromSalesforce();

    validateAndUpsertSalesforceOrganization();

    upsertSalesforceOAuthTokens();

    verifyExistingSalesforceUser();

    if (this.isNewUser) {
      fetchUserInfoFromSalesforce();
      createSalesforceUser();
    }

    return prepareResponse();
  }

  /**
   * Call Salesforce oauth token endpoint and fetch tokens.
   * 
   * @return void
   */
  private void fetchOauthTokensFromSalesforce() {
    logger.info("Fetching OAuth Tokens from Salesforce");

    HttpResponse response = salesforceGetTokens.getTokens(this.code, this.redirectUri);

    JsonNode jsonNode = util.getJsonNode(response.getResponseBody());

    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    SalesforceGetTokensDto salesforceGetTokensDto = mapper.convertValue(jsonNode,
        SalesforceGetTokensDto.class);

    this.tokensData = salesforceGetTokensDto;
  }

  /**
   * Validate and upsert Salesforce Organization in DB.
   * 
   * @return void
   */
  private void validateAndUpsertSalesforceOrganization() {
    logger.info("Validating Salesforce Organization");

    String salesforceOrganizationId = this.tokensData.getSalesforceOrganizationId();

    SalesforceOrganization existingOrganizationData = salesforceOrganizationRepository
        .getSalesforceOrganizationByExternalOrganizationId(salesforceOrganizationId);

    if (existingOrganizationData != null) {
      logger.info("Salesforce Organization already exists");

      if (existingOrganizationData.getStatus() != SalesforceOrganization.Status.ACTIVE) {
        logger.info("Salesforce Organization is not active.");
        throw new CustomException(
            new ErrorObject(
                "s_s_as_vauso_1",
                "forbidden_api_request",
                "Salesforce Organization is not active."));
      }
    }

    logger.info("Upserting Salesforce Organization in DB");
    SalesforceOrganization salesforceOrganization = new SalesforceOrganization();
    salesforceOrganization.setExternalOrganizationId(salesforceOrganizationId);
    salesforceOrganization.setStatus(SalesforceOrganization.Status.ACTIVE);

    salesforceOrganizationRepository
        .saveSalesforceOrganization(salesforceOrganization);
  }

  /**
   * Upsert Salesforce Oauth Token in DB.
   * 
   * @return void
   */
  private void upsertSalesforceOAuthTokens() {

    logger.info("Upserting Salesforce OAuth Tokens in DB");

    long currentTime = System.currentTimeMillis();
    String encryptedAccessToken = awsKms.encryptToken(this.tokensData.getAccessToken());

    logger.info("Time in ms for encryption : " + (System.currentTimeMillis() - currentTime));

    String encryptedRefreshToken = awsKms.encryptToken(this.tokensData.getRefreshToken());
    String salesforceUserId = this.tokensData.getSalesforceUserId();

    SalesforceOauthToken salesforceOauthToken = new SalesforceOauthToken();
    // Todo: use salesforceconnect res entity object
    salesforceOauthToken.setExternalUserId(salesforceUserId);
    salesforceOauthToken.setIdentityUrl(this.tokensData.getId());
    salesforceOauthToken.setAccessToken(encryptedAccessToken);
    salesforceOauthToken.setRefreshToken(encryptedRefreshToken);
    salesforceOauthToken.setSignature(this.tokensData.getSignature());
    salesforceOauthToken.setIdToken(this.tokensData.getIdToken());
    salesforceOauthToken.setInstanceUrl(this.tokensData.getInstanceUrl());
    salesforceOauthToken.setStatus(SalesforceOauthToken.Status.ACTIVE);
    salesforceOauthToken.setIssuedAt(Long.parseLong(this.tokensData.getIssuedAt()));

    this.salesforceOauthToken = salesforceOauthTokenRepository
        .saveSalesforceOauthToken(salesforceOauthToken);
  }

  /**
   * Verify if Salesforce User already exists.
   * 
   * @return void
   */
  private void verifyExistingSalesforceUser() {
    String salesforceUserId = this.tokensData.getSalesforceUserId();
    SalesforceUser salesforceUser = salesforceUserRepository.getSalesforceUserByExternalUserId(salesforceUserId);

    if (salesforceUser != null) {
      logger.info("Salesforce User already exists");
      this.salesforceUser = salesforceUser;
      this.isNewUser = false;
      this.decryptedSalt = localCipher.decrypt(CoreConstants.encryptionKey(), salesforceUser.getEncryptionSalt());
    }
  }

  /**
   * Fetch user info from Salesforce Identity API.
   * 
   * @return void
   */
  private void fetchUserInfoFromSalesforce() {

    logger.info("Calling Salesforce Identity API");

    HttpResponse response = salesforceGetIdentity.getUserIdentity(salesforceOauthToken.getInstanceUrl(),
        this.tokensData.getAccessToken());
    JsonNode jsonNode = util.getJsonNode(response.getResponseBody());
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    SalesforceGetIdentityDto salesforceGetIdentityDto = mapper.convertValue(jsonNode,
        SalesforceGetIdentityDto.class);

    this.userData = salesforceGetIdentityDto;
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

    String encryptedSalt = localCipher.encrypt(CoreConstants.encryptionKey(), decryptedSalt);
    String encryptedCookieToken = localCipher.encrypt(decryptedSalt, cookieToken);

    SalesforceUser salesforceUser = new SalesforceUser();
    salesforceUser.setExternalUserId(this.userData.getUserId());
    salesforceUser.setIdentityUrl(this.userData.getSub());
    salesforceUser.setExternalOrganizationId(this.userData.getOrganizationId());
    salesforceUser.setName(this.userData.getName());
    salesforceUser.setEmail(this.userData.getEmail());
    salesforceUser.setUserKind(UserConstants.SALESFORCE_USER_KIND);
    salesforceUser.setCookieToken(encryptedCookieToken);
    salesforceUser.setEncryptionSalt(encryptedSalt);
    salesforceUser.setStatus(SalesforceUser.Status.ACTIVE);

    this.salesforceUser = salesforceUserRepository.saveSalesforceUser(salesforceUser);
    this.decryptedSalt = decryptedSalt;
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
    currentUserEntityDto.setId(this.salesforceUser.getId(this.salesforceUser.getExternalUserId()));
    currentUserEntityDto.setName(this.salesforceUser.getName());
    currentUserEntityDto.setEmail(this.salesforceUser.getEmail());

    String userLoginCookieValue = cookieHelper.getCookieValue(this.salesforceUser,
        UserConstants.SALESFORCE_USER_KIND,
        this.decryptedSalt);

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