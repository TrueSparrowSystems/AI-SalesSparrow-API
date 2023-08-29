package com.salessparrow.api.lib.salesforce.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import java.util.List;

/**
 * SalesforceGetTokens class to get tokens from Salesforce
 */
@Component
public class SalesforceTokens {

  @Autowired
  private SalesforceConstants salesforceConstants;

  /**
   * Get tokens from Salesforce using the authorization code.
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
      List<String> paramErrorIdentifiers = new ArrayList<>();
      paramErrorIdentifiers.add("invalid_code");

      throw new CustomException(new ParamErrorObject(
          "l_s_w_sgt_gt_1",
          e.getMessage(),
          paramErrorIdentifiers));
    }
    return response;
  }

  /**
   * Revokes tokens from Salesforce using the access/refresh token.
   * 
   * @param instanceUrl Instance URL
   * @param token       Refresh token
   * 
   * @return HttpResponse
   */
  public HttpResponse revokeTokens(String instanceUrl, String token) {
    String salesforceRevokeTokensEndpoint = instanceUrl + salesforceConstants.revokeTokensUrl();

    String requestBody = "token=" + token;

    System.out.println("requestBody: " + requestBody);

    Map<String, String> headers = new HashMap<>();
    headers.put("content-type", "application/x-www-form-urlencoded");

    System.out.println("headers: " + headers);
    HttpResponse response = null;
    try {
      response = HttpClient.makePostRequest(
          salesforceRevokeTokensEndpoint,
          headers,
          requestBody,
          10000);
    } catch (Exception e) {
      List<String> paramErrorIdentifiers = new ArrayList<>();
      paramErrorIdentifiers.add("invalid_code");

      throw new CustomException(new ParamErrorObject(
          "l_s_w_sgt_gt_1",
          e.getMessage(),
          paramErrorIdentifiers));
    }
    return response;
  }
}
