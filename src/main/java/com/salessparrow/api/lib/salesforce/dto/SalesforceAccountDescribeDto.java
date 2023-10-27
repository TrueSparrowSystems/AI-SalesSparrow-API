package com.salessparrow.api.lib.salesforce.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when describing account.
 */
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SalesforceAccountDescribeDto {

	private Fields fields;

  @Data
  @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
  private class Fields {

    private String label;

    private String name;

    private String length;

    private String type;

    private String defaultValue;  

    private PicklistValues picklistValues;

    @Data
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    private class PicklistValues {

      private String label;

      private String value;

      private String active;

    }
  }


}
