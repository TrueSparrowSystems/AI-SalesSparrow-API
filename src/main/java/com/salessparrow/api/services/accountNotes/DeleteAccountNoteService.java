package com.salessparrow.api.services.accountNotes;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.crmActions.deleteAccountNote.DeleteAccountNoteFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * DeleteAccountNoteService is a service class for the DeleteAccountNote action for the CRM.
 */
@Service
public class DeleteAccountNoteService {
  private Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteAccountNoteService.class);

  @Autowired
  private DeleteAccountNoteFactory deleteAccountNoteFactory;
  
  /**
   * Delete note for the given note id
   * 
   * @param accountId
   * @param noteId
   * 
   * @return void
   */
  public void deleteAcountNote(HttpServletRequest request, String accountId, String noteId) {
      logger.info("Delete Account Note Service called");

      User currentUser = (User) request.getAttribute("current_user");
      
      deleteAccountNoteFactory.deleteAccountNote(currentUser, noteId);
  }
}
