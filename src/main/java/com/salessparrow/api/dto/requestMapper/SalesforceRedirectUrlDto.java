package com.salessparrow.api.dto.requestMapper;

import com.salessparrow.api.lib.customAnnotations.ValidRedirectUri;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Redirect url DTO.
 *
 * @param redirect_uri
 * @param state
 * @return SalesforceRedirectUrlDto
 */
@Data
public class SalesforceRedirectUrlDto {

	@NotBlank(message = "missing_redirect_uri")
	@ValidRedirectUri(message = "invalid_redirect_uri")
	private String redirect_uri;

	private String state;

}
