package com.salessparrow.api.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceUser;

@Repository
public class SalesforceUserRepository {

  @Autowired
  private DynamoDBMapper dynamoDBMapper;

  public SalesforceUser saveSalesforceUser(SalesforceUser salesforceUser) {
    dynamoDBMapper.save(salesforceUser);
    return salesforceUser;
  }
}