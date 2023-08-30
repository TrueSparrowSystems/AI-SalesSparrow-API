package com.salessparrow.api.lib.salesforce.dto;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when creating a note.
 */
@Data
public class SalesforceCreateNoteDto {

	String id;

	String success;

	String[] errors;

}
