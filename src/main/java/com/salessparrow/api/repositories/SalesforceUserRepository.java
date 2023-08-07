package com.salessparrow.api.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.salessparrow.api.domain.SalesforceUser;

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
    dynamoDBMapper.save(salesforceUser);
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
  public SalesforceUser getSalesforceUserById(String id) {
    return dynamoDBMapper.load(SalesforceUser.class, id);
  }

  /**
   * Retrieves a SalesforceUser from the salesforce_users table based on the
   * provided externalUserId.
   * 
   * @param externalUserId
   * 
   * @return SalesforceUser
   */
  public SalesforceUser getSalesforceUserByExternalUserId(String externalUserId) {
    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(":val", new AttributeValue().withS(externalUserId));

    DynamoDBQueryExpression<SalesforceUser> queryExpression = new DynamoDBQueryExpression<SalesforceUser>()
        .withConsistentRead(false)
        .withIndexName("external_user_id_index")
        .withKeyConditionExpression("external_user_id= :val")
        .withExpressionAttributeValues(expressionAttributeValues);

    PaginatedQueryList<SalesforceUser> queryResult = dynamoDBMapper.query(SalesforceUser.class, queryExpression);

    if (queryResult != null && !queryResult.isEmpty()) {
      return queryResult.get(0);
    } else {
      return null;
    }
  }
}
