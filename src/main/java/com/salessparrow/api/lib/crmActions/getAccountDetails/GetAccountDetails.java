package com.salessparrow.api.lib.crmActions.getAccountDetails;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountDetailsFormatterDto;

/**
 * GetAccountDetails is an interface for the getAccountDetails action for the CRM.
 */
@Component
public interface GetAccountDetails {

	public GetAccountDetailsFormatterDto getAccountDetails(User user, String accountId);

}
