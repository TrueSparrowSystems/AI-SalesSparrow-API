package com.salessparrow.api.lib.crmActions.getAccountNoteDetails;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;

/**
 * GetNoteDetails is an interface for the GetNoteDetails action for the CRM.
 */
@Component
public interface GetAccountNoteDetails {
    public GetNoteDetailsFormatterDto getNoteDetails(User user, String noteId);
}
