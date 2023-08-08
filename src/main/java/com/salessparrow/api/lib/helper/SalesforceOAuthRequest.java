package com.salessparrow.api.lib.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.ErrorObject;
import com.salessparrow.api.lib.salesforce.SalesforceOAuthAccessToken;

@Component
public class SalesforceOAuthRequest {

  @Autowired
  private SalesforceOAuthAccessToken getAccessTokenService;

  public <T> T makeRequest(String salesforceUserId, SalesforceOAuthRequestInterface<T> request) throws Exception {
    String decryptedAccessToken = getAccessTokenService.fetchAccessToken(salesforceUserId);

    try {
      return request.execute(decryptedAccessToken);
    } catch (Exception e) {
      try {
        decryptedAccessToken = getAccessTokenService.updateAccessToken(salesforceUserId);
        return request.execute(decryptedAccessToken);
      } catch (Exception e1) {
        throw new CustomException(
          new ErrorObject(
            "l_h_soar_mr_1",
            "something_went_wrong",
            e.getMessage()));
      }
    }
  }
}
