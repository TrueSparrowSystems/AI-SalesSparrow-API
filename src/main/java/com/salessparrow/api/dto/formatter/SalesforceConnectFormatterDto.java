package com.salessparrow.api.dto.formatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.CurrentUserEntityDto;

import lombok.Data;

/**
 * Salesforce connect formatter DTO.
 * 
 * @param current_user
 * 
 * @return SalesforceConnectFormatterDto
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SalesforceConnectFormatterDto {
  public SalesforceConnectFormatterDto() {
  }

  private CurrentUserEntityDto currentUser;

}
