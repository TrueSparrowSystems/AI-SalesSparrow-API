package com.salessparrow.api.lib.salesforce.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SalesforceGetTokensDto {

	private String accessToken;

	private String refreshToken;

	private String signature;

	private String scope;

	private String idToken;

	private String instanceUrl;

	private String id;

	private String tokenType;

	private String issuedAt;

	/**
	 * Get Salesforce organization id from id
	 * @return String
	 */
	public String getSalesforceOrganizationId() {
		String[] idParts = id.split("/");
		return idParts[4];
	}

	/**
	 * Get Salesforce user id from id
	 * @return String
	 */
	public String getSalesforceUserId() {
		String[] idParts = id.split("/");
		return idParts[5];
	}

}
