package com.salessparrow.api.lib.crmActions.getNoteDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;

/**
 * GetNoteDetailsFactory is a factory class for the GetNoteDetails action for the CRM.
 */
@Component
public class GetNoteDetailsFactory {
    @Autowired 
    GetSalesforceNoteDeatails getSalesforceNoteDeatails;

    /**
     * getNoteDetails is a method that returns the details of a note.
     * 
     * @param user
     * @param noteId
     * 
     * @return GetNoteDetailsFormatterDto
     */
    public GetNoteDetailsFormatterDto getNoteDetails(SalesforceUser user, String noteId) {
        
        GetNoteDetailsFormatterDto getNoteDetailsResponse = null;

        getNoteDetailsResponse = getSalesforceNoteDeatails.getNoteDetails(user, noteId);
        return getNoteDetailsResponse;
    }
}
