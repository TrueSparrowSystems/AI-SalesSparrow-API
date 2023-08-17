package com.salessparrow.api.lib.salesforce.dto;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when attaching a note to an account.
 */
@Data
public class SalesforceAttachNoteDto {
  String id;

  String success;

  String[] errors;
}
