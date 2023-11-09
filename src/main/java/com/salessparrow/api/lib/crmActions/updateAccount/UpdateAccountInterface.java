package com.salessparrow.api.lib.crmActions.updateAccount;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;

/**
 * UpdateAccountInterface is an interface for the update account action for the CRM.
 */
@Component
public interface UpdateAccountInterface {

	public void updateAccount(User user, String accountId, Map<String, String> updateAccountDto);

}
