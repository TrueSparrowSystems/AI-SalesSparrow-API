package com.salessparrow.api.lib.crmActions.updateAccountNote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.AccountNoteDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Base64Helper;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * UpdateSalesforceAccountNote is a class that updates a note for an account in
 * Salesforce.
 */
@Component
public class UpdateSalesforceAccountNote implements UpdateAccountNoteInterface {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateSalesforceAccountNote.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private Base64Helper base64Helper;

	/**
	 * Update a note for a given account.
	 * @param user
	 * @param accountId
	 * @param accountNoteDto
	 * @param noteId
	 * @return void
	 */
	public void updateNote(User user, String accountId, String noteId, AccountNoteDto accountNoteDto) {
		logger.info("Update Salesforce Note started");
		String salesforceUserId = user.getExternalUserId();

		String noteContent = accountNoteDto.getText();
		String unEscapeNoteContent = Util.unEscapeSpecialCharactersForPlainText(noteContent);
		String noteTitle = Util.getTrimmedString(unEscapeNoteContent,
				salesforceConstants.salesforceContentNoteTitleLength());

		noteContent = Util.replaceNewLineWithBreak(noteContent);
		String encodedNoteContent = base64Helper.base64Encode(noteContent);

		Map<String, String> updateNoteBody = new HashMap<String, String>();
		updateNoteBody.put("Title", noteTitle);
		updateNoteBody.put("Content", encodedNoteContent);

		CompositeRequestDto updateNoteCompositeRequestDto = new CompositeRequestDto("PATCH",
				salesforceConstants.salesforceUpdateNoteUrl(noteId), "UpdateNote", updateNoteBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(updateNoteCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param updateNoteResponse
	 * @return void
	 */
	private void parseResponse(String updateNoteResponse) {
		logger.info("Parsing the response from Salesforce");

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(updateNoteResponse);

		JsonNode compositeResponse = rootNode.get("compositeResponse").get(0);
		Integer statusCode = compositeResponse.get("httpStatusCode").asInt();

		if (statusCode != 200 && statusCode != 201 && statusCode != 204) {
			String errorBody = compositeResponse.get("body").asText();

			// MALFORMED_ID or NOT_FOUND
			if (statusCode == 400 || statusCode == 404) {

				throw new CustomException(
						new ParamErrorObject("l_ua_uan_usan_pr_1", errorBody, Arrays.asList("invalid_note_id")));
			}
			else {
				throw new CustomException(new ErrorObject("l_ua_uan_usan_pr_2", "something_went_wrong", errorBody));
			}
		}
	}

}
