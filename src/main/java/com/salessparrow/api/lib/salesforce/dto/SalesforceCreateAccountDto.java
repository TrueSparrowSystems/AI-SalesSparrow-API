package com.salessparrow.api.lib.salesforce.dto;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when creating an account.
 */
@Data
public class SalesforceCreateAccountDto {

	String id;

	String success;

	String[] errors;

}
