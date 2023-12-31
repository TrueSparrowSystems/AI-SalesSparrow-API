package com.salessparrow.api.lib.crmActions.createAccountNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.dto.requestMapper.AccountNoteDto;
import com.salessparrow.api.lib.Base64Helper;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCreateNoteDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * CreateSalesforceNote is a class that creates a note in Salesforce and attaches it to an
 * account.
 */
@Component
public class CreateSalesforceNote implements CreateNoteInterface {

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private Base64Helper base64Helper;

	Logger logger = LoggerFactory.getLogger(CreateSalesforceNote.class);

	/**
	 * Create a note for a given account.
	 * @param user
	 * @param accountId
	 * @param note
	 * @return CreateNoteFormatterDto
	 */
	public CreateNoteFormatterDto createNote(SalesforceUser user, String accountId, AccountNoteDto note) {
		String salesforceUserId = user.getExternalUserId();

		String noteContent = note.getText();
		String unEscapeNoteContent = Util.unEscapeSpecialCharactersForPlainText(noteContent);
		String noteTitle = Util.getTrimmedString(unEscapeNoteContent,
				salesforceConstants.salesforceContentNoteTitleLength());

		noteContent = Util.replaceNewLineWithBreak(noteContent);
		String encodedNoteContent = base64Helper.base64Encode(noteContent);

		Map<String, String> createNoteBody = new HashMap<String, String>();
		createNoteBody.put("Title", noteTitle);
		createNoteBody.put("Content", encodedNoteContent);

		CompositeRequestDto createNoteCompositeRequestDto = new CompositeRequestDto("POST",
				salesforceConstants.salesforceCreateNoteUrl(), "CreateNote", createNoteBody);

		Map<String, String> attachNoteBody = new HashMap<String, String>();
		attachNoteBody.put("ContentDocumentId", "@{CreateNote.id}");
		attachNoteBody.put("LinkedEntityId", accountId);

		CompositeRequestDto attachNoteCompositeRequestDto = new CompositeRequestDto("POST",
				salesforceConstants.salesforceAttachNoteUrl(), "AttachNote", attachNoteBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(createNoteCompositeRequestDto);
		compositeRequests.add(attachNoteCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param createNoteFormatterDto
	 * @return CreateNoteFormatterDto - formatted response
	 */
	private CreateNoteFormatterDto parseResponse(String createNoteResponse) {
		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(createNoteResponse);

		JsonNode createNoteCompositeResponse = rootNode.get("compositeResponse").get(0);
		String createNoteStatusCode = createNoteCompositeResponse.get("httpStatusCode").asText();

		if (!createNoteStatusCode.equals("200") && !createNoteStatusCode.equals("201")) {
			String errorBody = createNoteCompositeResponse.get("body").asText();

			logger.error("createNoteCompositeResponse: " + createNoteCompositeResponse);
			throw new CustomException(new ErrorObject("l_s_fse_fscn_fcn_1", "internal_server_error", errorBody));
		}

		JsonNode createNoteNodeResponseBody = rootNode.get("compositeResponse").get(0).get("body");

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		SalesforceCreateNoteDto salesforceCreateNoteDto = mapper.convertValue(createNoteNodeResponseBody,
				SalesforceCreateNoteDto.class);

		JsonNode attachNoteCompositeResponse = rootNode.get("compositeResponse").get(1);
		String attachNoteStatusCode = attachNoteCompositeResponse.get("httpStatusCode").asText();

		if (!attachNoteStatusCode.equals("200") && !attachNoteStatusCode.equals("201")) {
			String errorBody = attachNoteCompositeResponse.get("body").toString();

			throw new CustomException(new ErrorObject("l_s_fse_fscn_fcn_2", "internal_server_error", errorBody));
		}

		CreateNoteFormatterDto createNoteFormatterDto = new CreateNoteFormatterDto();
		createNoteFormatterDto.setNoteId(salesforceCreateNoteDto.getId());

		return createNoteFormatterDto;
	}

}
