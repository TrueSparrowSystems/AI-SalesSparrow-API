package com.salessparrow.api.lib.crmActions.describeAccount;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.DescribeAccountFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * DescribeAccountFactory is a factory class for the DescribeAccount action for the CRM.
 */
@Component
public class DescribeAccountFactory {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DescribeAccountFactory.class);

	@Autowired
	private DescribeSalesforceAccount describeSalesforceAccount;

	/**
	 * Get the Description the Account Fields.
	 * @param user
	 * @return DescribeAccountFormatterDto
	 **/
	public DescribeAccountFormatterDto describeAccount(User user) {
		logger.info("Describe Account Factory called");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return describeSalesforceAccount.describeAccount(user);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_da_daf_da_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
