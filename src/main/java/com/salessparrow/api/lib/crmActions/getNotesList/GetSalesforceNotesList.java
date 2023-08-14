package com.salessparrow.api.lib.crmActions.getNotesList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceNotesList;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

/**
 * GetSalesforceNotesList is a class for the GetNotesList service for the Salesforce CRM.
 */
@Component
public class GetSalesforceNotesList implements GetNotesList{
    @Autowired
    private SalesforceConstants salesforceConstants;

    @Autowired
    private MakeCompositeRequest makeCompositeRequest;

    /**
     * Get the list of notes for a given account
     * @param accountId
     * @param salesforceUserId
     * 
     * @return HttpResponse
     **/
    private HttpClient.HttpResponse getDocumentIds(String accountId, String salesforceUserId) {
        SalesforceQueryBuilder salesforceLib = new SalesforceQueryBuilder();
        String documentIdsQuery = salesforceLib.getContentDocumentIdUrl(accountId);

        String documentIdsUrl = salesforceConstants.queryUrlPath() + documentIdsQuery;

        CompositeRequestDto documentIdsCompositeReq = new CompositeRequestDto("GET", documentIdsUrl, "GetContentDocumentId");

        List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
        compositeRequests.add(documentIdsCompositeReq);

        HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId );

        return response;
    }

    /**
     * Get the list of notes for a given account
     * @param documentIds
     * @param salesforceUserId
     * 
     * @return HttpResponse
     **/
    private HttpClient.HttpResponse getNotes(List<String> documentIds, String salesforceUserId) {
        SalesforceQueryBuilder salesforceLib = new SalesforceQueryBuilder();
        String notesQuery = salesforceLib.getNoteListIdUrl(documentIds);

        String notesUrl = salesforceConstants.queryUrlPath() + notesQuery;

        CompositeRequestDto noteCompositeRequest = new CompositeRequestDto("GET", notesUrl, "GetNotesList");

        List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
        compositeRequests.add(noteCompositeRequest);

        HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId );

        return response;
    }

    /**
     * Get the list of notes for a given account
     * @param user
     * @param accountId
     * 
     * @return GetNotesListFormatterDto
     **/
    public GetNotesListFormatterDto getNotesList(User user,String accountId) {

        String salesforceUserId = user.getExternalUserId();

        HttpClient.HttpResponse response = getDocumentIds(accountId, salesforceUserId);

        FormatSalesforceNotesList formatSalesforceNotesList = new FormatSalesforceNotesList();
        List<String> ContentDocumentIds = formatSalesforceNotesList.formatContentDocumentId(response.getResponseBody());

        HttpClient.HttpResponse getNotesResponse = getNotes(ContentDocumentIds, salesforceUserId);

        GetNotesListFormatterDto getNotesListFormatterDto = formatSalesforceNotesList.formatNotesList(getNotesResponse.getResponseBody());

        return getNotesListFormatterDto;
    }

}
