package com.salessparrow.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CacheConstants;

/**
 * Repository for SalesforceOauthToken.
 */
@Repository
public class SalesforceOauthTokenRepository {

  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  /**
   * Saves a SalesforceOauthToken to the salesforce_oauth_tokens table.
   * 
   * @param salesforceOauthToken
   * 
   * @return SalesforceOauthToken
   */
  @CacheEvict(value = CacheConstants.SS_SALESFORCE_OAUTH_TOKEN_CACHE, key = "#salesforceOauthToken.externalUserId")
  public SalesforceOauthToken saveSalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
    try {
      dynamoDBMapper.save(salesforceOauthToken);
    } catch (Exception e) {
      throw new CustomException(new ErrorObject(
          "r_sotr_ssot_1",
          "something_went_wrong",
          e.getMessage()));
    }
    return salesforceOauthToken;
  }

  /**
   * Retrieves a SalesforceOauthToken from the salesforce_oauth_tokens table based
   * on the
   * provided externalUserId.
   * 
   * @param externalUserId
   * 
   * @return SalesforceOauthToken
   */
  @Cacheable(value = CacheConstants.SS_SALESFORCE_OAUTH_TOKEN_CACHE, key = "#externalUserId")
  public SalesforceOauthToken getSalesforceOauthTokenByExternalUserId(String externalUserId) {
    try {
      return dynamoDBMapper.load(SalesforceOauthToken.class, externalUserId);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "r_sotr_gsotbsfui_1",
              "something_went_wrong",
              e.getMessage()));
    }
  }
}
