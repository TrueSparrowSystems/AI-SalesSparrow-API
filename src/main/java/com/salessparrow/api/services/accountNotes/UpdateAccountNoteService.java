package com.salessparrow.api.services.accountNotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.AccountNoteDto;
import com.salessparrow.api.lib.crmActions.updateAccountNote.UpdateAccountNoteFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * UpdateAccountNoteService is a service class that handles the update of a note in an
 * account.
 */
@Service
public class UpdateAccountNoteService {

	Logger logger = LoggerFactory.getLogger(UpdateAccountNoteService.class);

	@Autowired
	private UpdateAccountNoteFactory updateAccountNoteFactory;

	/**
	 * Updates a note in an account.
	 * @param request
	 * @param accountId
	 * @param noteId
	 * @return void
	 */
	public void updateAccountNote(HttpServletRequest request, String accountId, String noteId,
			AccountNoteDto accountNoteDto) {
		logger.info("Update note in account service called");

		User currentUser = (User) request.getAttribute("current_user");

		updateAccountNoteFactory.updateNote(currentUser, accountId, noteId, accountNoteDto);
	}

}
