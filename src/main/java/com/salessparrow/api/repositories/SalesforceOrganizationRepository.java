package com.salessparrow.api.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

import org.springframework.stereotype.Repository;

/**
 * Repository for SalesforceOrganization.
 */
@Repository
public class SalesforceOrganizationRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public SalesforceOrganizationRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	/**
	 * Saves a SalesforceOrganization to the salesforce_organizations table.
	 * @param sfo
	 * @return SalesforceOrganization
	 */
	public SalesforceOrganization saveSalesforceOrganization(SalesforceOrganization sfo) {
		try {
			dynamoDBMapper.save(sfo);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sor_sso_1", "something_went_wrong", e.getMessage()));
		}
		return sfo;
	}

	/**
	 * Gets a SalesforceOrganization from the salesforce_organizations table by
	 * externalOrganizationId.
	 * @param externalOrganizationId
	 * @return SalesforceOrganization
	 */
	public SalesforceOrganization getSalesforceOrganizationByExternalOrganizationId(String externalOrganizationId) {
		try {
			return dynamoDBMapper.load(SalesforceOrganization.class, externalOrganizationId);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sor_gsobeoi_1", "something_went_wrong", e.getMessage()));
		}
	}

}
