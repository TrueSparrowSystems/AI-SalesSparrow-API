package com.salessparrow.api.dto.requestMapper;

import com.salessparrow.api.lib.customAnnotations.ValidRedirectUri;

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

  @NotBlank(message = "missing_code")
  private String code;

  @NotBlank(message = "missing_redirect_uri")
  @ValidRedirectUri(message = "invalid_redirect_uri")
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

  @Override
  public String toString() {
    return "SalesforceConnectDto [code=" + code + ", redirect_uri=" + redirect_uri + "]";
  }

}
