package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountDetailsFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccountDetails.GetAccountDetailsFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountDetailsService is a service class for the getAccountDetails action for the
 * CRM.
 */
@Service
public class GetAccountDetailsService {

	@Autowired
	private GetAccountDetailsFactory getAccountDetailsFactory;

	/**
	 * Get the details of an account
	 * @param request
	 * @param accountId
	 * @return GetAccountDetailsFormatterDto
	 */
	public GetAccountDetailsFormatterDto getAccountDetails(HttpServletRequest request, String accountId) {
		User currentUser = (User) request.getAttribute("current_user");

		return getAccountDetailsFactory.getAccountDetails(currentUser, accountId);
	}

}
