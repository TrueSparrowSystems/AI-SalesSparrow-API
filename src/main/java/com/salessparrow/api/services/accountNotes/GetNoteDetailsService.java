package com.salessparrow.api.services.accountNotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.lib.crmActions.getNoteDetails.GetNoteDetailsFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetNoteDetailsService is a service class for the GetNoteDetails action for the CRM.
 */
@Service
public class GetNoteDetailsService {
  @Autowired
  private GetNoteDetailsFactory getNoteDetailsFactory;

  /**
   * Get the details of a note
   * 
   * @param accountId
   * @param noteId
   * 
   * @return GetNoteDetailsFormatterDto
   */
  public GetNoteDetailsFormatterDto getNoteDetails(HttpServletRequest request, String noteId) {
      User currentUser = (User) request.getAttribute("current_user");
      
      return getNoteDetailsFactory.getNoteDetails(currentUser, noteId);
  }
}
