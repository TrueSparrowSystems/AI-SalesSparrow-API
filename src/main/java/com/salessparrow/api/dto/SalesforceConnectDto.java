package com.salessparrow.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Salesforce connect DTO.
 * 
 * @param code
 * @param redirect_uri
 * 
 * @return SalesforceConnectDto
 */
public class SalesforceConnectDto {

  @NotBlank(message = "Code is required")
  private String code;

  @NotBlank(message = "Redirect URI is required")
  private String redirect_uri;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getRedirect_uri() {
    return redirect_uri;
  }

  public void setRedirect_uri(String redirect_uri) {
    this.redirect_uri = redirect_uri;
  }

}
