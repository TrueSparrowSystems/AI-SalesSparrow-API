package com.salessparrow.api.lib.crmActions.getAccountDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetAccountDetailsFactory is a factory class for the getAccountDetails action for the
 * CRM.
 */
@Component
public class GetAccountDetailsFactory {

	@Autowired
	GetSalesforceAccountDetails getSalesforceAccountDetails;

	/**
	 * getAccountDetails is a method that returns the details of an account.
	 * @param user
	 * @param accountId
	 * @return GetAccountDetailsFormatterDto
	 */
	public GetAccountDetailsFormatterDto getAccountDetails(User user, String accountId) {

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return getSalesforceAccountDetails.getAccountDetails(user, accountId);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_gad_gadf_gad_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
