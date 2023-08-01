package com.salessparrow.api.dto.formatter;

public class LoginServiceDto {
  private SafeUserDto user;

  public SafeUserDto getUser() {
    return user;
  }

  public void setUser(SafeUserDto user) {
    this.user = user;
  }
}
