package com.salessparrow.api.lib.crmActions.updateAccountNote;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.AccountNoteDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * UpdateAccountNoteFactory is a factory class for the update note action for the CRM.
 */
@Component
public class UpdateAccountNoteFactory {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateAccountNoteFactory.class);

	@Autowired
	private UpdateSalesforceAccountNote updateSalesforceNote;

	/**
	 * Update a note for a given account
	 * @param user
	 * @param accountId
	 * @return void
	 **/
	public void updateNote(User user, String accountId, String noteId, AccountNoteDto accountNoteDto) {
		logger.info("Update Note Factory started");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				updateSalesforceNote.updateNote(user, accountId, noteId, accountNoteDto);
				return;
			default:
				throw new CustomException(
						new ErrorObject("l_ua_uan_uanf_uan_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
