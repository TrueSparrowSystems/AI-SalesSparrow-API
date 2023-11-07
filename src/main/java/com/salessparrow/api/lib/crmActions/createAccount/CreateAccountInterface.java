package com.salessparrow.api.lib.crmActions.createAccount;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateAccountFormatterDto;

/**
 * CreateAccountInterface is an interface for the create account action for the CRM.
 */
@Component
public interface CreateAccountInterface {

	public CreateAccountFormatterDto createAccount(User user, Map<String, String> createAccountDto);

}
