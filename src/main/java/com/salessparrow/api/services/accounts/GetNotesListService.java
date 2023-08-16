package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.lib.crmActions.getNotesList.GetNoteListFactory;

/**
 * GetNotesListService is a service class for the GetNotesList action for the CRM.
 */
@Service
public class GetNotesListService {
    @Autowired
    private GetNoteListFactory getNotesListFactory;

    /**
     * Get the list of notes for a given account
     * @param accountId
     * @param request
     * 
     * @return GetNotesListFormatterDto
     **/
    public GetNotesListFormatterDto getNotesList(HttpServletRequest request, String accountId) {

        User user = (User) request.getAttribute("user");

        return getNotesListFactory.getNotesList(user, accountId);
    }

    
}
