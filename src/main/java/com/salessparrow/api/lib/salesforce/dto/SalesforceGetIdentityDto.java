package com.salessparrow.api.lib.salesforce.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SalesforceGetIdentityDto {
  private String sub;
  private String userId;
  private String organizationId;
  private String name;
  private String email;
}
