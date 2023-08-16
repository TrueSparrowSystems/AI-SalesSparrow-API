package com.salessparrow.api.dto.formatter;

import com.salessparrow.api.dto.entities.CurrentUserEntityDto;

/**
 * Get current user formatter DTO.
 * 
 * @param current_user
 * 
 * @return GetCurrentUserFormatterDto
 */
public class GetCurrentUserFormatterDto {
  private CurrentUserEntityDto current_user;

  public CurrentUserEntityDto getCurrent_user() {
    return current_user;
  }

  public void setCurrent_user(CurrentUserEntityDto current_user) {
    this.current_user = current_user;
  }
}