package com.salessparrow.api.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.salessparrow.api.domain.SalesforceOauthToken;

/**
 * Repository for SalesforceOauthToken.
 */
@Repository
public class SalesforceOauthTokenRepository {

  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  public SalesforceOauthToken saveSalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
    dynamoDBMapper.save(salesforceOauthToken);
    return salesforceOauthToken;
  }

  public SalesforceOauthToken upsertSalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
    dynamoDBMapper.save(salesforceOauthToken, new DynamoDBSaveExpression().withExpectedEntry("id",
        new ExpectedAttributeValue(new AttributeValue().withS(salesforceOauthToken.getId()))));
    return salesforceOauthToken;
  }

  public SalesforceOauthToken getSalesforceOauthTokenBySalesforceUserId(String salesforceUserId) {
    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(":val", new AttributeValue().withS(salesforceUserId));

    DynamoDBQueryExpression<SalesforceOauthToken> queryExpression = new DynamoDBQueryExpression<SalesforceOauthToken>()
        .withConsistentRead(false)
        .withIndexName("salesforce_user_id_index")
        .withKeyConditionExpression("salesforce_user_id= :val")
        .withExpressionAttributeValues(expressionAttributeValues);

    PaginatedQueryList<SalesforceOauthToken> queryResult = dynamoDBMapper.query(SalesforceOauthToken.class,
        queryExpression);

    if (queryResult != null && !queryResult.isEmpty()) {
      return queryResult.get(0);
    } else {
      return null;
    }
  }

}
