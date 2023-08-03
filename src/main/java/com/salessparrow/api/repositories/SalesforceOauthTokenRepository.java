package com.salessparrow.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceOauthToken;

@Repository
public class SalesforceOauthTokenRepository {

  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  public SalesforceOauthToken saveSalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
    dynamoDBMapper.save(salesforceOauthToken);
    return salesforceOauthToken;
  }
}
