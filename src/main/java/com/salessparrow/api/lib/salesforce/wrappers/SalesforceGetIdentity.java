package com.salessparrow.api.lib.salesforce.wrappers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;

@Component

public class SalesforceGetIdentity {

	@Autowired
	private SalesforceConstants salesforceConstants;

	public HttpResponse getUserIdentity(String instanceUrl, String accessToken) {
		String salesforceIdentityEndpoint = instanceUrl + salesforceConstants.identityUrl();

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + accessToken);

		HttpResponse response = null;
		try {
			response = HttpClient.makeGetRequest(salesforceIdentityEndpoint, headers, 10000);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_s_w_sgi_gui_1", "bad_request", e.getMessage()));
		}

		return response;
	}

}
