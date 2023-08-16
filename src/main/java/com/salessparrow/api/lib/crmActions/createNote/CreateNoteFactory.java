package com.salessparrow.api.lib.crmActions.createNote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.NoteDto;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;

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
        CreateNoteFormatterDto createNoteResponse = createSalesforceNote.createNote(user, accountId, note);

        return createNoteResponse;
    }
}
