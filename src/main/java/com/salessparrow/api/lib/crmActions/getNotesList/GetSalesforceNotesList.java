package com.salessparrow.api.lib.crmActions.getNotesList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.CompositeRequest;
import com.salessparrow.api.lib.salesforce.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.SalesforceQueries;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceNotesList;

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
    public HttpClient.HttpResponse getDocumentIds(String accountId, String salesforceUserId) {
        SalesforceQueries salesforceLib = new SalesforceQueries();
        String documentIdsQuery = salesforceLib.getContentDocumentIdUrl(accountId);

        String documentIdsUrl = salesforceConstants.queryUrlPath() + documentIdsQuery;

        CompositeRequest documentIdsCompositeReq = new CompositeRequest("GET", documentIdsUrl, "GetContentDocumentId");

        List<CompositeRequest> compositeRequests = new ArrayList<CompositeRequest>();
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
    public HttpClient.HttpResponse getNotes(List<String> documentIds, String salesforceUserId) {
        SalesforceQueries salesforceLib = new SalesforceQueries();
        String notesQuery = salesforceLib.getNoteListIdUrl(documentIds);

        String notesUrl = salesforceConstants.queryUrlPath() + notesQuery;

        CompositeRequest noteCompositeRequest = new CompositeRequest("GET", notesUrl, "GetNotesList");

        List<CompositeRequest> compositeRequests = new ArrayList<CompositeRequest>();
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
    public GetNotesListFormatterDto getNotesList(SalesforceUser user,String accountId) {

        String salesforceUserId = user.getExternalUserId();

        HttpClient.HttpResponse response = getDocumentIds(accountId, salesforceUserId);

        FormatSalesforceNotesList formatSalesforceNotesList = new FormatSalesforceNotesList();
        List<String> ContentDocumentIds = formatSalesforceNotesList.formatContentDocumentId(response.getResponseBody());

        HttpClient.HttpResponse getNotesResponse = getNotes(ContentDocumentIds, salesforceUserId);

        GetNotesListFormatterDto getNotesListFormatterDto = formatSalesforceNotesList.formatNotesList(getNotesResponse.getResponseBody());

        return getNotesListFormatterDto;
    }

}
