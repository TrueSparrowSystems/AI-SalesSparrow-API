package com.salessparrow.api.lib.salesforce.wrappers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;

/**
 * SalesforceGetTokens class to get tokens from Salesforce
 */
@Component
public class SalesforceGetTokens {

  @Autowired
  private SalesforceConstants salesforceConstants;

  /**
   * Get tokens from Salesforce
   * 
   * @param code
   * @param redirectUri
   * 
   * @return HttpResponse
   */
  public HttpResponse getTokens(String code, String redirectUri) {

    String salesforceOAuthEndpoint = salesforceConstants.oauth2Url();

    String requestBody = "grant_type=" + salesforceConstants.authorizationCodeGrantType() + "&client_id="
        + CoreConstants.salesforceClientId()
        + "&client_secret="
        + CoreConstants.salesforceClientSecret() +
        "&code=" + code + "&redirect_uri=" + redirectUri;

    Map<String, String> headers = new HashMap<>();
    headers.put("content-type", "application/x-www-form-urlencoded");

    HttpResponse response = null;
    try {
      response = HttpClient.makePostRequest(
          salesforceOAuthEndpoint,
          headers,
          requestBody,
          10000);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "l_s_w_sgt_1",
              "bad_request",
              e.getMessage()));
    }
    return response;
  }
}
