package com.salessparrow.api.dto.formatter;

import com.salessparrow.api.dto.entities.CurrentUserEntityDto;

/**
 * Salesforce connect formatter DTO.
 * 
 * @param current_user
 * 
 * @return SalesforceConnectFormatterDto
 */
public class SalesforceConnectFormatterDto {
  public SalesforceConnectFormatterDto() {
  }

  private CurrentUserEntityDto current_user;

  public CurrentUserEntityDto getCurrent_user() {
    return current_user;
  }

  public void setCurrent_user(CurrentUserEntityDto current_user) {
    this.current_user = current_user;
  }

}
