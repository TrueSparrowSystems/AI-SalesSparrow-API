package com.salessparrow.api.lib.salesforce.wrappers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;

/**
 * SalesforceGetTokens class to get access token by refresh token from Salesforce
 */
@Component
public class SalesforceGetRefreshedAccessToken {

	@Autowired
	SalesforceConstants salesforceConstants;

	/**
	 * Get tokens from Salesforce
	 * @param decryptedRefreshToken
	 * @return HttpResponse
	 */
	public HttpClient.HttpResponse getRefreshedAccessToken(String decryptedRefreshToken) {
		String url = salesforceConstants.oauth2Url();

		String requestBody = "grant_type=" + salesforceConstants.refreshTokenGrantType() + "&client_id="
				+ CoreConstants.salesforceClientId() + "&client_secret=" + CoreConstants.salesforceClientSecret()
				+ "&refresh_token=" + decryptedRefreshToken;

		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/x-www-form-urlencoded");

		HttpClient.HttpResponse response = HttpClient.makePostRequest(url, headers, requestBody, 5000);

		return response;
	}

}
