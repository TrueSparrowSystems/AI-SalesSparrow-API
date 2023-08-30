package com.salessparrow.api.lib.salesforce.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

/**
 * SalesforceRequest is a class for making a request to the Salesforce API.
 */
@Component
public class SalesforceRequest {

	@Autowired
	private SalesforceOAuthToken getAccessTokenService;

	@Autowired
	private SalesforceOauthTokenRepository salesforceOauthTokenRepository;

	/**
	 * Make a request to the Salesforce API.
	 * @param salesforceUserId
	 * @param request
	 * @return T
	 */
	public <T> T makeRequest(String salesforceUserId, SalesforceRequestInterface<T> request) {
		SalesforceOauthToken sfOAuthToken = salesforceOauthTokenRepository
			.getSalesforceOauthTokenByExternalUserId(salesforceUserId);

		String decryptedAccessToken = getAccessTokenService.fetchAccessToken(sfOAuthToken);

		try {
			return request.execute(decryptedAccessToken, sfOAuthToken.getInstanceUrl());
		}
		catch (WebClientResponseException e) {
			if (e.getStatusCode().value() == 401) {
				try {
					decryptedAccessToken = getAccessTokenService.updateAndGetRefreshedAccessToken(sfOAuthToken);
					return request.execute(decryptedAccessToken, sfOAuthToken.getInstanceUrl());
				}
				catch (Exception e1) {
					throw new CustomException(new ErrorObject("l_s_h_sr_mr_1", "something_went_wrong", e.getMessage()));
				}
			}
			throw new CustomException(new ErrorObject("l_s_h_sr_mr_2", "something_went_wrong", e.getMessage()));
		}
	}

}