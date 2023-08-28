package com.salessparrow.api.lib.crmActions.deleteAccountNote;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * DeleteAccountNoteFactory is a factory class for the DeleteAccountNote action for the CRM.
 */
@Component
public class DeleteAccountNoteFactory {
  private Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteAccountNoteFactory.class);

  @Autowired 
  DeleteSalesforceAccountNote getSalesforceNoteDetails;

  @Autowired
  DeleteSalesforceAccountNote deleteAccountSalesforceNote;

  /**
   * deleteAccountNote is a method that makes call to delete note based on user kind.
   * 
   * @param user
   * @param noteId
   * 
   * @return void
   */
  public void deleteAccountNote(User user, String noteId) {
    logger.info("Delete Account Note Factory called");

    switch(user.getUserKind()) {
    case UserConstants.SALESFORCE_USER_KIND:
      deleteAccountSalesforceNote.deleteAccountNote(user, noteId);
      break;
    default:
      throw new CustomException(
        new ErrorObject(
          "l_ca_dan_danf_dn_1",
          "something_went_wrong",
          "Invalid user kind."));
    }
  }
}
