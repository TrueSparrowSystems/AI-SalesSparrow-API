package com.salessparrow.api.lib.salesforce.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;

/**
 * MakeCompositeRequest is a class for making a composite request to the Salesforce API.
 **/
@Component
public class MakeCompositeRequest {

	Logger logger = LoggerFactory.getLogger(MakeCompositeRequest.class);

	@Autowired
	private SalesforceRequest salesforceOauthRequest;

	@Autowired
	private SalesforceConstants salesforceConstants;

	/**
	 * Make composite post request to the Salesforce API.
	 * @param compositeRequests
	 * @param salesforceUserId
	 * @return HttpClient.HttpResponse
	 **/
	public HttpClient.HttpResponse makePostRequest(List<CompositeRequestDto> compositeRequests,
			String salesforceUserId) {
		Map<String, List<CompositeRequestDto>> compositeRequestsMap = new HashMap<>();
		compositeRequestsMap.put("compositeRequest", compositeRequests);

		Integer timeoutMillis = salesforceConstants.timeoutMillis();

		SalesforceRequestInterface<HttpClient.HttpResponse> request = (token, instanceUrl) -> {
			String httpReqUrl = salesforceConstants.salesforceCompositeUrl(instanceUrl);

			Map<String, String> headers = new HashMap<>();
			headers.put("Authorization", "Bearer " + token);

			HttpClient.HttpResponse response = HttpClient.makePostRequest(httpReqUrl, headers, compositeRequestsMap,
					timeoutMillis);
			return response;
		};

		HttpClient.HttpResponse response = null;

		logger.info("making composite request to salesforce");

		response = salesforceOauthRequest.makeRequest(salesforceUserId, request);
		return response;
	}

}
