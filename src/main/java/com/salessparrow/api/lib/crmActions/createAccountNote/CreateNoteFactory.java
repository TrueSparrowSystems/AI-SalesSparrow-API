package com.salessparrow.api.lib.crmActions.createAccountNote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;
import com.salessparrow.api.dto.requestMapper.NoteDto;

/**
 * CreateNoteFactory is a factory class for the create note action for the CRM.
 */
@Component
public class CreateNoteFactory {
	@Autowired
	private CreateSalesforceNote createSalesforceNote;

	/**
	 * Create a note for a given account
	 * @param user
	 * @param accountId
	 * 
	 * @return CreateNoteFormatterDto
	 **/
	public CreateNoteFormatterDto createNote(SalesforceUser user, String accountId, NoteDto note) {

		switch(user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return createSalesforceNote.createNote(user, accountId, note);
			default:
				throw new CustomException(
					new ErrorObject(
						"l_ca_cn_cnf_cn_1",
						"something_went_wrong",
						"Invalid user kind."));
		}
	}
}
