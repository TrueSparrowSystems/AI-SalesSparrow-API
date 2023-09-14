package com.salessparrow.api.lib.crmActions.getCrmOrganizationUsers;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetCrmOrganizationUsersFormatterDto;

/**
 * GetCrmOrganizationUsers interface for the getCrmOrganizationUsers action.
 *
 */
@Component
public interface GetCrmOrganizationUsers {

	public GetCrmOrganizationUsersFormatterDto getCrmOrganizationUsers(User user, String searchTerm);

}
