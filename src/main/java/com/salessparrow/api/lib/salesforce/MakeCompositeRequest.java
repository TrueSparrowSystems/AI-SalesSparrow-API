package com.salessparrow.api.lib.salesforce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.helper.SalesforceOAuthRequest;
import com.salessparrow.api.lib.helper.SalesforceOAuthRequestInterface;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

@Component
public class MakeCompositeRequest {

  @Autowired
  private SalesforceOAuthRequest salesforceOauthRequest;

  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private SalesforceOauthTokenRepository sfOauthTokenRepository;

  public HttpClient.HttpResponse makePostRequest(
    List<CompositeRequest> compositeRequests,
    String salesforceUserId
  ) {
    Map<String, List<CompositeRequest>> compositeRequestsMap = new HashMap<>();
    compositeRequestsMap.put("compositeRequest", compositeRequests);

    SalesforceOauthToken sfOAuthToken = sfOauthTokenRepository.getSalesforceOauthTokenBySalesforceUserId(salesforceUserId);

    String httpReqUrl = salesforceConstants.salesforceCompositeUrl(sfOAuthToken.getInstanceUrl());
    Integer timeoutMillis = salesforceConstants.timeoutMillis();

    SalesforceOAuthRequestInterface<HttpClient.HttpResponse> request = token -> {
      Map<String,String> headers = new HashMap<>();
      headers.put("Authorization", "Bearer " + token);

      HttpClient.HttpResponse response = HttpClient.makePostRequest(
        httpReqUrl, 
        headers, 
        compositeRequestsMap, 
        timeoutMillis
      );
      return response;
    };

    HttpClient.HttpResponse response = null;
    try {
      response = salesforceOauthRequest.makeRequest(salesforceUserId, request);
    } catch (Exception e) {
      throw new CustomException(
        new ErrorObject(
          "s_mcr_mpr_1",
          "something_went_wrong",
          e.getMessage()));
    }
    return response;
  }
}
