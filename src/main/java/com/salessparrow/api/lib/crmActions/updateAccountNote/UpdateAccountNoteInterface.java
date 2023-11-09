package com.salessparrow.api.lib.crmActions.updateAccountNote;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.AccountNoteDto;

/**
 * UpdateAccountNoteInterface is an interface for the update note action for the CRM.
 */
@Component
public interface UpdateAccountNoteInterface {

	public void updateNote(User user, String accountId, String noteId, AccountNoteDto noteDto);

}
