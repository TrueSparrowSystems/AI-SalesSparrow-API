package com.salessparrow.api.lib.crmActions.getNoteDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetNoteDetailsFactory is a factory class for the GetNoteDetails action for the CRM.
 */
@Component
public class GetNoteDetailsFactory {
  @Autowired 
  GetSalesforceNoteDetails getSalesforceNoteDetails;

  /**
   * getNoteDetails is a method that returns the details of a note.
   * 
   * @param user
   * @param noteId
   * 
   * @return GetNoteDetailsFormatterDto
   */
  public GetNoteDetailsFormatterDto getNoteDetails(User user, String noteId) {

    switch(user.getUserKind()) {
    case UserConstants.SALESFORCE_USER_KIND:
      return getSalesforceNoteDetails.getNoteDetails(user, noteId);
    default:
      throw new CustomException(
        new ErrorObject(
          "l_ca_gnd_gndf_gnd_1",
          "something_went_wrong",
          "Invalid user kind."));
    }
  }
}
