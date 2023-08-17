package com.salessparrow.api.dto.requestMapper;

import com.salessparrow.api.lib.customAnnotations.ValidRedirectUri;

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

    @NotBlank(message = "missing_redirect_uri")
    @ValidRedirectUri(message = "invalid_redirect_uri")
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

    @Override
    public String toString() {
        return "SalesforceRedirectUrlDto [redirect_uri=" + redirect_uri + ", state=" + state + "]";
    }
}
