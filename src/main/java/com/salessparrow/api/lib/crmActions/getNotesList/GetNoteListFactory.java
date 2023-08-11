package com.salessparrow.api.lib.crmActions.getNotesList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;

/**
 * GetNotesListFactory is a factory class for the GetNotesList action for the
 * CRM.
 */
@Component
public class GetNoteListFactory {
    @Autowired
    private GetSalesforceNotesList getSalesforceNotesList;

    /**
     * Get the list of notes for a given account.
     * 
     * @param user
     * @param accountId
     * 
     * @return GetNotesListFormatterDto
     **/
    public GetNotesListFormatterDto getNotesList(SalesforceUser user, String accountId) {

        GetNotesListFormatterDto getNotesListResponse = null;
        getNotesListResponse = getSalesforceNotesList.getNotesList(user, accountId);

        return getNotesListResponse;
    }
}
