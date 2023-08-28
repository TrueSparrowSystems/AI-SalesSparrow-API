package com.salessparrow.api.lib.crmActions.deleteAccountNote;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;

/**
 * DeleteAccountNoteInterface is an interface for the DeleteAccountNote action for the CRM.
 */
@Component
public interface DeleteAccountNoteInterface {
    public void deleteAccountNote(User user, String noteId);
}
