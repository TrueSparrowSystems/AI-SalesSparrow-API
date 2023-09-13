package com.salessparrow.api.lib.salesforce.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import java.util.List;

/**
 * SalesforceTokens class to handle token operations with Salesforce
 */
@Component
public class SalesforceTokens {

	@Autowired
	private SalesforceConstants salesforceConstants;

	/**
	 * Get tokens from Salesforce using the authorization code.
	 * @param code
	 * @param redirectUri
	 * @return HttpResponse
	 */
	public HttpResponse getTokens(String code, String redirectUri, Boolean isTestUser) {

		String salesforceOAuthEndpoint = salesforceConstants.oauth2Url();

		String requestBody;

		if (!isTestUser) {
			requestBody = String.format("grant_type=%s&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
					salesforceConstants.authorizationCodeGrantType(), CoreConstants.salesforceClientId(),
					CoreConstants.salesforceClientSecret(), code, redirectUri);
		}
		else {
			requestBody = String.format(
					"grant_type=%s&client_id=%s&client_secret=%s&username=%s&password=%s&redirect_uri=%s",
					salesforceConstants.passwordGrantType(), CoreConstants.salesforceClientId(),
					CoreConstants.salesforceClientSecret(), CoreConstants.defaultTestUser(),
					CoreConstants.defaultTestUserPassword(), redirectUri);
		}

		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/x-www-form-urlencoded");

		HttpResponse response = null;
		try {
			response = HttpClient.makePostRequest(salesforceOAuthEndpoint, headers, requestBody, 10000);
		}
		catch (Exception e) {
			List<String> paramErrorIdentifiers = new ArrayList<>();
			paramErrorIdentifiers.add("invalid_code");

			throw new CustomException(new ParamErrorObject("l_s_w_sgt_gt_1", e.getMessage(), paramErrorIdentifiers));
		}
		return response;
	}

	/**
	 * Revokes tokens from Salesforce using the access/refresh token.
	 * @param instanceUrl Instance URL
	 * @param token Refresh token
	 * @return HttpResponse
	 */
	public HttpResponse revokeTokens(String instanceUrl, String token) {
		String salesforceRevokeTokensEndpoint = instanceUrl + salesforceConstants.revokeTokensUrl();

		String requestBody = "token=" + token;

		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/x-www-form-urlencoded");

		HttpResponse response = null;
		try {
			response = HttpClient.makePostRequest(salesforceRevokeTokensEndpoint, headers, requestBody, 10000);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_s_w_srt_rt_1", "something_went_wrong", e.getMessage()));
		}
		return response;
	}

}