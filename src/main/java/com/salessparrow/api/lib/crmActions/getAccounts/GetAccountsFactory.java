package com.salessparrow.api.lib.crmActions.getAccounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetAccountsFactory is a factory class for the GetAccounts action for the CRM.
 */
@Component
public class GetAccountsFactory {
  @Autowired
  private GetSalesforceAccounts getSalesforceAccounts;

  public GetAccountsFormatterDto getAccounts(User user, String searchTerm) {

    switch(user.getUserKind()) {
      case UserConstants.SALESFORCE_USER_KIND:
        return getSalesforceAccounts.getAccounts(user, searchTerm);
      default:
        throw new CustomException(
          new ErrorObject(
            "l_ca_ga_gaf_ga_1",
            "something_went_wrong",
            "Invalid user kind."));
    }
  }
}
