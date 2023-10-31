package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.DescribeAccountFormatterDto;
import com.salessparrow.api.lib.crmActions.describeAccount.DescribeAccountFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * DescribeAccountService is a service class for the DescribeAccount action for the CRM.
 */
@Service
public class DescribeAccountService {

	@Autowired
	private DescribeAccountFactory describeAccountFactory;

	/**
	 * Describe account fields
	 * @param request
	 * @return DescribeAccountFormatterDto
	 **/
	public DescribeAccountFormatterDto describeAccount(HttpServletRequest request) {
		User currentUser = (User) request.getAttribute("current_user");

		DescribeAccountFormatterDto describeAccountFormatterDto = describeAccountFactory.describeAccount(currentUser);

		return describeAccountFormatterDto;
	}

}