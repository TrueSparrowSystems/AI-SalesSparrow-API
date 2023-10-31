package com.salessparrow.api.lib.crmActions.describeAccount;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.DescribeAccountFormatterDto;

/**
 * DescribeAccountInterface is an interface for the DescribeAccount action for the CRM.
 */
@Component
public interface DescribeAccountInterface {

	public DescribeAccountFormatterDto describeAccount(User user);

}
