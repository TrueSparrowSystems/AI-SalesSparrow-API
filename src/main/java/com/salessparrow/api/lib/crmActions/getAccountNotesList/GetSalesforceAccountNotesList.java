package com.salessparrow.api.lib.crmActions.getAccountNotesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.NoteEntity;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetNoteIdDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetNotesListDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

/**
 * GetSalesforceNotesList is a class for the GetNotesList service for the Salesforce CRM.
 */
@Component
public class GetSalesforceAccountNotesList implements GetAccountNotesList {

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Get the list of notes for a given account
	 * @param user
	 * @param accountId
	 * @return GetNotesListFormatterDto
	 **/
	public GetNotesListFormatterDto getNotesList(User user, String accountId) {

		String salesforceUserId = user.getExternalUserId();

		HttpClient.HttpResponse response = notesListIdResponse(accountId, salesforceUserId);

		List<String> ContentDocumentIds = parseNotesId(response.getResponseBody());

		if (ContentDocumentIds.size() == 0) {
			return new GetNotesListFormatterDto(new ArrayList<String>(), new HashMap<String, NoteEntity>());
		}

		HttpClient.HttpResponse getNotesResponse = notesListByIdResponse(ContentDocumentIds, salesforceUserId);

		GetNotesListFormatterDto getNotesListFormatterDto = parseResponse(getNotesResponse.getResponseBody());

		return getNotesListFormatterDto;
	}

	/**
	 * Get the list of notes id for a given account
	 * @param accountId
	 * @param salesforceUserId
	 * @return HttpResponse
	 **/
	private HttpClient.HttpResponse notesListIdResponse(String accountId, String salesforceUserId) {
		SalesforceQueryBuilder salesforceLib = new SalesforceQueryBuilder();
		String documentIdsQuery = salesforceLib.getContentDocumentIdUrl(accountId);

		String documentIdsUrl = salesforceConstants.queryUrlPath() + documentIdsQuery;

		CompositeRequestDto documentIdsCompositeReq = new CompositeRequestDto("GET", documentIdsUrl,
				"GetContentDocumentId");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(documentIdsCompositeReq);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return response;
	}

	/**
	 * Get the list of notes for a given account
	 * @param documentIds
	 * @param salesforceUserId
	 * @return HttpResponse
	 **/
	private HttpClient.HttpResponse notesListByIdResponse(List<String> documentIds, String salesforceUserId) {
		SalesforceQueryBuilder salesforceLib = new SalesforceQueryBuilder();
		String notesQuery = salesforceLib.getNoteListIdUrl(documentIds);

		String notesUrl = salesforceConstants.queryUrlPath() + notesQuery;

		CompositeRequestDto noteCompositeRequest = new CompositeRequestDto("GET", notesUrl, "GetNotesList");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(noteCompositeRequest);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return response;
	}

	/**
	 * Get the list of notes for a given account
	 * @param responseBody
	 * @return List<String>
	 */
	private List<String> parseNotesId(String responseBody) {
		List<String> notesIds = new ArrayList<String>();

		try {
			Util util = new Util();
			JsonNode rootNode = util.getJsonNode(responseBody);
			JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");
			for (JsonNode recordNode : recordsNode) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				SalesforceGetNoteIdDto salesforceGetNoteId = mapper.convertValue(recordNode,
						SalesforceGetNoteIdDto.class);
				notesIds.add(salesforceGetNoteId.getContentDocumentId());
			}
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_c_gnl_gsnl_1", "something_went_wrong", e.getMessage()));
		}

		return notesIds;
	}

	/**
	 * Get the list of notes for a given account
	 * @param responseBody
	 * @return GetNotesListFormatterDto
	 */
	private GetNotesListFormatterDto parseResponse(String responseBody) {
		List<String> noteIds = new ArrayList<String>();
		Map<String, NoteEntity> noteIdToEntityMap = new HashMap<>();

		try {
			Util util = new Util();
			JsonNode rootNode = util.getJsonNode(responseBody);
			JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");
			for (JsonNode recordNode : recordsNode) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				SalesforceGetNotesListDto salesforceGetNotesList = mapper.convertValue(recordNode,
						SalesforceGetNotesListDto.class);
				NoteEntity noteEntity = salesforceGetNotesList.noteEntity();

				noteIds.add(noteEntity.getId());
				noteIdToEntityMap.put(noteEntity.getId(), noteEntity);
			}
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_c_gnl_gsnl_2", "something_went_wrong", e.getMessage()));
		}

		GetNotesListFormatterDto getNotesListFormatterDto = new GetNotesListFormatterDto();
		getNotesListFormatterDto.setNoteIds(noteIds);
		getNotesListFormatterDto.setNoteMapById(noteIdToEntityMap);

		return getNotesListFormatterDto;
	}

}
