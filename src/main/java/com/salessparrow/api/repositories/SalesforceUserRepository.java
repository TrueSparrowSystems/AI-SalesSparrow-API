package com.salessparrow.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

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
