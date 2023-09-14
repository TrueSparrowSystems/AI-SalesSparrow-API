package com.salessparrow.api.lib.crmActions.createAccountNote;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.dto.requestMapper.NoteDto;

/**
 * CreateNoteInterface is an interface for the create note action for the CRM.
 */
@Component
public interface CreateNoteInterface {

	public CreateNoteFormatterDto createNote(SalesforceUser user, String accountId, NoteDto note);

}
