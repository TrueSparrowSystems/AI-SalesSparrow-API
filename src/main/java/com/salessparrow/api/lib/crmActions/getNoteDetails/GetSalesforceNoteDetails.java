package com.salessparrow.api.lib.crmActions.getNoteDetails;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.NoteDetailEntity;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetNoteDetailsDto;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetNoteContent;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

@Component
public class GetSalesforceNoteDetails implements GetNoteDetails {

    @Autowired
    private SalesforceConstants salesforceConstants;

    @Autowired
    private MakeCompositeRequest makeCompositeRequest;

    @Autowired
    private SalesforceGetNoteContent salesforceGetNoteContent;

    /**
     * Get the list of notes for a given account
     * 
     * @param documentIds
     * @param salesforceUserId
     * 
     * @return HttpResponse
     **/
    private HttpClient.HttpResponse getNotes(String noteId, String salesforceUserId) {
        SalesforceQueryBuilder salesforceLib = new SalesforceQueryBuilder();
        String notesQuery = salesforceLib.getNoteDetailsUrl(noteId);

        String notesUrl = salesforceConstants.queryUrlPath() + notesQuery;

        CompositeRequestDto noteCompositeRequest = new CompositeRequestDto("GET", notesUrl, "GetNotesList");

        List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
        compositeRequests.add(noteCompositeRequest);

        HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

        return response;
    }

    /**
     * Get the details of a note
     * 
     * @param user
     * @param noteId
     * 
     * @return GetNoteDetailsFormatterDto
     **/
    public GetNoteDetailsFormatterDto getNoteDetails(User user, String noteId) {

        String salesforceUserId = user.getExternalUserId();

        HttpClient.HttpResponse noteDetailsResponse = getNotes(noteId, salesforceUserId);

        HttpClient.HttpResponse noteContentResponse = salesforceGetNoteContent.getNoteContent(noteId, salesforceUserId);

        GetNoteDetailsFormatterDto noteDetailsFormatterDto = formatNoteDetails(noteDetailsResponse.getResponseBody(), noteContentResponse.getResponseBody());

        return noteDetailsFormatterDto;

    }

    private GetNoteDetailsFormatterDto formatNoteDetails(String noteDetailsResponse, String noteContentResponse) {
        NoteDetailEntity noteDetailEntity = new NoteDetailEntity();
        try {
            Util util = new Util();
            JsonNode rootNode = util.getJsonNode(noteDetailsResponse);
            JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

            for (JsonNode recordNode : recordsNode) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                SalesforceGetNoteDetailsDto salesforceGetNotesList = mapper.convertValue(recordNode, SalesforceGetNoteDetailsDto.class);
                noteDetailEntity = salesforceGetNotesList.noteDetailEntity(noteContentResponse);
            }
        } catch (Exception e) {
            throw new CustomException(
                new ErrorObject(
                    "l_c_gnd_gsnd_1",
                    "something_went_wrong",
                    e.getMessage()
                )
            );
        }

        GetNoteDetailsFormatterDto getNoteDetailsFormatterDto = new GetNoteDetailsFormatterDto(
            noteDetailEntity
        );

        return getNoteDetailsFormatterDto;

    }

}
