package com.salessparrow.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CacheConstants;

/**
 * Repository for SalesforceUser.
 */
@Repository
public class SalesforceUserRepository {

  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  /**
   * Saves a SalesforceUser to the salesforce_users table.
   * 
   * @param salesforceUser
   * 
   * @return SalesforceUser
   */
  @CachePut(value=CacheConstants.SALESFORCE_USER_CACHE, key="#salesforceUser.externalUserId")
  public SalesforceUser saveSalesforceUser(SalesforceUser salesforceUser) {
    try {
      dynamoDBMapper.save(salesforceUser);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "r_sur_ssu_1",
              "something_went_wrong",
              e.getMessage()));
    }
    return salesforceUser;
  }

  /**
   * Retrieves a SalesforceUser from the salesforce_users table based on the
   * provided id.
   * 
   * @param id
   * 
   * @return SalesforceUser
   */
  @Cacheable(value=CacheConstants.SALESFORCE_USER_CACHE, key="#externalUserId")
  public SalesforceUser getSalesforceUserByExternalUserId(String externalUserId) {
    try {
      return dynamoDBMapper.load(SalesforceUser.class, externalUserId);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "r_sur_gsubi_1",
              "something_went_wrong",
              e.getMessage()));
    }
  }
}
