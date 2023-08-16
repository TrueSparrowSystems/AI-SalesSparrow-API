package com.salessparrow.api.lib.crmActions.createNote;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.NoteDto;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;

/**
 * CreateNoteInterface is an interface for the create note action for the CRM.
 */
@Component
public interface CreateNoteInterface {
    public CreateNoteFormatterDto createNote(SalesforceUser user, String accountId, NoteDto note);
}

