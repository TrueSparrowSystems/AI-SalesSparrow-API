package com.salessparrow.api.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

/**
 * Redirect url DTO.
 * 
 * @param redirect_uri
 * @param state
 * 
 * @return SalesforceRedirectUrlDto
 */
public class SalesforceRedirectUrlDto {
	@NotBlank(message = "redirect_uri is required")
	@URL(message = "Invalid redirect_uri format")
	private String redirect_uri;
	
  private String state;

  public String getRedirectUri() {
      return redirect_uri;
  }

  public void setRedirectUri(String redirect_uri) {
      this.redirect_uri = redirect_uri;
  }

  public String getState() {
      return state;
  }

  public void setState(String state) {
      this.state = state;
  }

  public SalesforceRedirectUrlDto(String redirect_uri, String state) {
      this.redirect_uri = redirect_uri;
      this.state = state;
  }
}
