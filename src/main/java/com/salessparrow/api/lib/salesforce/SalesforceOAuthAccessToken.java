package com.salessparrow.api.lib.salesforce;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

@Service
public class SalesforceOAuthAccessToken {

  @Autowired
  private AwsKms awsKms;

  @Autowired
  private CoreConstants coreConstants;
  @Autowired
  private SalesforceConstants salesforceConstants;
  @Autowired
  private SalesforceOauthTokenRepository sfOauthTokenRepository;
  @Autowired
  private Util util;

  public String fetchAccessToken(String salesforceUserId) {
    SalesforceOauthToken sfOAuthToken = sfOauthTokenRepository.getSalesforceOauthTokenBySalesforceUserId(salesforceUserId);

    String decryptedAccessToken = awsKms.decryptToken(sfOAuthToken.getAccessToken());

    return decryptedAccessToken;
  }

  public String updateAccessToken(String salesforceUserId) {
    SalesforceOauthToken sfOAuthToken = sfOauthTokenRepository.getSalesforceOauthTokenBySalesforceUserId(salesforceUserId);
    String encryptedRefreshToken = sfOAuthToken.getRefreshToken();
    String decryptedRefreshToken = awsKms.decryptToken(encryptedRefreshToken);

    String url = salesforceConstants.oauth2Url();
    System.out.println(salesforceConstants.oauth2Url());

    String requestBody = "grant_type=" + salesforceConstants.refreshTokenGrantType() + "&client_id="
        + coreConstants.salesforceClientId()
        + "&client_secret="
        + coreConstants.salesforceClientSecret() +
        "&refresh_token=" + decryptedRefreshToken;

    Map<String, String> headers = new HashMap<>();
    headers.put("content-type", "application/x-www-form-urlencoded");

    HttpClient.HttpResponse response = HttpClient.makePostRequest(url, headers, requestBody, 5000);
    JsonNode rootNode = util.getJsonNode(response.getResponseBody());
    String decryptedAccessToken = rootNode.get("access_token").asText();

    return decryptedAccessToken;
  }
}
