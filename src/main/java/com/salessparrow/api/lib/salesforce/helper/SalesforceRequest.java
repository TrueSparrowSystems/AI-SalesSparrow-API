package com.salessparrow.api.lib.salesforce.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

@Component
public class SalesforceRequest {

  @Autowired
  private SalesforceOAuthToken getAccessTokenService;

  public <T> T makeRequest(String salesforceUserId, SalesforceRequestInterface<T> request) {
    String decryptedAccessToken = getAccessTokenService.fetchAccessToken(salesforceUserId);

    try {
      return request.execute(decryptedAccessToken);
    } catch (Exception e) {
      try {
        decryptedAccessToken = getAccessTokenService.updateAndGetRefreshedAccessToken(salesforceUserId);
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
