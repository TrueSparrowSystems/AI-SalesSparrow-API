package com.salessparrow.api.lib.crmActions.disconnectUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * Factory for disconnecting a user from the CRM based on the user kind.
 */
@Component
public class DisconnectUserFactory {

  @Autowired
  private DisconnectSalesforceUser disconnectSalesforceUser;

  /**
   * Disconnect a user from the CRM based on the user kind.
   * 
   * @param user
   * 
   * @return void
   */
  public void disconnect(User user) {

    switch (user.getUserKind()) {
      case UserConstants.SALESFORCE_USER_KIND:
        disconnectSalesforceUser.disconnect(user);
        break;
      default:
        throw new CustomException(
            new ErrorObject(
                "l_ca_du_duf_d_1",
                "something_went_wrong",
                "Invalid user kind."));
    }
  }
}
