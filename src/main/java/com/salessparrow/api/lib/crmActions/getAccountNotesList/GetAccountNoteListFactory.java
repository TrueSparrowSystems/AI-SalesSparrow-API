package com.salessparrow.api.lib.crmActions.getAccountNotesList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetNotesListFactory is a factory class for the GetNotesList action for the
 * CRM.
 */
@Component
public class GetAccountNoteListFactory {
    @Autowired
    private GetSalesforceAccountNotesList getSalesforceNotesList;

    /**
     * Get the list of notes for a given account.
     * 
     * @param user
     * @param accountId
     * 
     * @return GetNotesListFormatterDto
     **/
    public GetNotesListFormatterDto getNotesList(User user, String accountId) {

      switch(user.getUserKind()) {
        case UserConstants.SALESFORCE_USER_KIND:
          return getSalesforceNotesList.getNotesList(user, accountId);
        default:
          throw new CustomException(
            new ErrorObject(
              "l_ca_gnl_gnlf_gnl_1",
              "something_went_wrong",
              "Invalid user kind."));
        }
    }
}
