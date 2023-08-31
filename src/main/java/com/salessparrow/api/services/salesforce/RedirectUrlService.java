package com.salessparrow.api.services.salesforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.dto.formatter.RedirectUrlFormatterDto;
import com.salessparrow.api.dto.requestMapper.SalesforceRedirectUrlDto;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;

/**
 * Service class for the to get the redirect url for the oauth2 flow.
 */
@Service
public class RedirectUrlService {

	@Autowired
	private SalesforceConstants salesforceConstants;

	Logger logger = LoggerFactory.getLogger(RedirectUrlService.class);

	/**
	 * Get the redirect url for the oauth2 flow
	 * @param salesforceRedirectUrlDto
	 * @return RedirectUrlFormatterDto
	 **/
	public RedirectUrlFormatterDto getSalesforceOauthUrl(SalesforceRedirectUrlDto salesforceRedirectUrlDto) {

		String redirectUri = salesforceRedirectUrlDto.getRedirect_uri();
		String state = salesforceRedirectUrlDto.getState();

		String salesforceLoginUrl = salesforceConstants.oauth2AuthorizeUrl();
		String salesforceClientId = CoreConstants.salesforceClientId();

		String salesforceOauthUrl = salesforceLoginUrl + "?response_type=code" + "&client_id=" + salesforceClientId
				+ "&redirect_uri=" + redirectUri;

		if (state != null && !state.isEmpty()) {
			salesforceOauthUrl += "&state=" + state;
		}

		RedirectUrlFormatterDto redirectUrlFormatterDto = new RedirectUrlFormatterDto();
		redirectUrlFormatterDto.setUrl(salesforceOauthUrl);

		return redirectUrlFormatterDto;
	}

}