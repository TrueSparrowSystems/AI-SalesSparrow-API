package com.salessparrow.api.lib.salesforce.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

@Component
public class MakeCompositeRequest {

  @Autowired
  private SalesforceRequest salesforceOauthRequest;

  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private SalesforceOauthTokenRepository sfOauthTokenRepository;

  public HttpClient.HttpResponse makePostRequest(
      List<CompositeRequestDto> compositeRequests,
      String salesforceUserId) {
    Map<String, List<CompositeRequestDto>> compositeRequestsMap = new HashMap<>();
    compositeRequestsMap.put("compositeRequest", compositeRequests);

    SalesforceOauthToken sfOAuthToken = sfOauthTokenRepository
        .getSalesforceOauthTokenByExternalUserId(salesforceUserId);

    String httpReqUrl = salesforceConstants.salesforceCompositeUrl(sfOAuthToken.getInstanceUrl());
    Integer timeoutMillis = salesforceConstants.timeoutMillis();

    SalesforceRequestInterface<HttpClient.HttpResponse> request = token -> {
      Map<String, String> headers = new HashMap<>();
      headers.put("Authorization", "Bearer " + token);

      HttpClient.HttpResponse response = HttpClient.makePostRequest(
          httpReqUrl,
          headers,
          compositeRequestsMap,
          timeoutMillis);
      return response;
    };

    HttpClient.HttpResponse response = null;
    
    response = salesforceOauthRequest.makeRequest(salesforceUserId, request);
    return response;
  }
}
