package com.salessparrow.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {
  @NotBlank(message = "Password is required")
  private String password;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  // Getters and setters

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDto( String email, String password) {
    this.email = email;
    this.password = password;
  }
}