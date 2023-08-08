package com.salessparrow.api.lib.salesforce;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.httpLib.HttpClient;

@Component
public class SalesforceClient {

  @Autowired
  private AwsKms awsKms;

  @Autowired
  private CoreConstants coreConstants;

  public <T> T makeRequest(String userId, SalesforceRequest<T> request) throws Exception {
    String decryptedAccessToken = fetchAccessToken(userId);

    try {
      return request.execute(decryptedAccessToken);
    } catch (Exception e) {
      // Refresh token and retry
      try {
        decryptedAccessToken = updateAccessToken(userId);
        return request.execute(decryptedAccessToken);
      } catch (Exception e1) {
        throw new CustomException(
            new ErrorObject(
                "l_s_sc_1",
                "something_went_wrong",
                e.getMessage()));
      }
    }
  }

  public String fetchAccessToken(String userId) {
    // TODO - Raj: fetch the refresh token from the database
    String encryptedAccessToken = "encryptedAccessToken";
    String decryptedAccessToken = awsKms.decryptToken(encryptedAccessToken);

    return decryptedAccessToken;
  }

  public String updateAccessToken(String userId) {
    // TODO - fetch the refresh token from the database
    String encryptedRefreshToken = "encryptedRefreshToken";
    String decryptedRefreshToken = awsKms.decryptToken(encryptedRefreshToken);

    String url = coreConstants.salesforceAuthUrl() + "/services/oauth2/token";
    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("grant_type", "refresh_token");
    requestBody.put("client_id", coreConstants.salesforceClientId());
    requestBody.put("refresh_token", decryptedRefreshToken);
    requestBody.put("client_secret", coreConstants.salesforceClientSecret());

    HttpClient.HttpResponse response = HttpClient.makePostRequest(url, null, requestBody, 5000);
    // TODO - Raj: Get Access token from response
    // TODO - encrypt the access token and save it to the database
    String decryptedAccessToken = "YOUR ACCESS TOKEN";

    return decryptedAccessToken;
  }
}
