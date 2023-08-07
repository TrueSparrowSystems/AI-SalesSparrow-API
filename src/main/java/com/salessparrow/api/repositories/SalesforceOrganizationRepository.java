package com.salessparrow.api.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.salessparrow.api.domain.SalesforceOrganization;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Repository for SalesforceOrganization.
 */
@Repository
public class SalesforceOrganizationRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Saves a SalesforceOrganization to the salesforce_organizations table.
     * 
     * @param sfo
     * 
     * @return SalesforceOrganization
     */
    public SalesforceOrganization saveSalesforceOrganization(SalesforceOrganization sfo) {
        dynamoDBMapper.save(sfo);
        return sfo;
    }

    /**
     * Retrieves a SalesforceOrganization from the salesforce_organizations table
     * based on externalOrganizationId.
     * 
     * @param externalOrganizationId
     * 
     * @return SalesforceOrganization
     */
    public SalesforceOrganization getSalesforceOrganizationByExternalOrganizationId(String externalOrganizationId) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(externalOrganizationId));

        DynamoDBQueryExpression<SalesforceOrganization> queryExpression = new DynamoDBQueryExpression<SalesforceOrganization>()
                .withConsistentRead(false)
                .withIndexName("external_organization_id_index")
                .withKeyConditionExpression("external_organization_id= :val")
                .withExpressionAttributeValues(expressionAttributeValues);

        PaginatedQueryList<SalesforceOrganization> queryResult = dynamoDBMapper.query(SalesforceOrganization.class,
                queryExpression);

        if (queryResult != null && !queryResult.isEmpty()) {
            return queryResult.get(0);
        } else {
            return null;
        }
    }
}
