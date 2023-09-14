package com.salessparrow.api.lib.crmActions.getCrmOrganizationUsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetCrmOrganizationUsersFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetCrmOrganizationUsersFactory class for the getCrmOrganizationUsers action.
 *
 */
@Component
public class GetCrmOrganizationUsersFactory {

	Logger logger = LoggerFactory.getLogger(GetCrmOrganizationUsersFactory.class);

	@Autowired
	private GetSalesforceCrmOrganizationUsers getSalesforceCrmOrganizationUsers;

	/**
	 * getCrmOrganizationUsers method for the getCrmOrganizationUsers action.
	 * @param user
	 * @param searchTerm
	 * @return GetCrmOrganizationUsersFormatterDto
	 */
	public GetCrmOrganizationUsersFormatterDto getCrmOrganizationUsers(User user, String searchTerm) {
		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				logger.info("Executing salesforce get CrmOrganizationUser Service");
				return getSalesforceCrmOrganizationUsers.getCrmOrganizationUsers(user, searchTerm);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_gcou_gcouf_gcou_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
