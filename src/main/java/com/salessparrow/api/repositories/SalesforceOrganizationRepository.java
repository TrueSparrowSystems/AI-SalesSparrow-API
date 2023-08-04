package com.salessparrow.api.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceOrganization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SalesforceOrganizationRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public SalesforceOrganization saveSalesforceOrganization(SalesforceOrganization sfo) {
        dynamoDBMapper.save(sfo);
        return sfo;
    }

    public SalesforceOrganization getById(String id) {
        return dynamoDBMapper.load(SalesforceOrganization.class, id);
    }
}
