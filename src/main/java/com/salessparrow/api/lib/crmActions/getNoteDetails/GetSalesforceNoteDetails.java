package com.salessparrow.api.lib.crmActions.getNoteDetails;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequest;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceNoteDetails;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueries;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetNoteContent;

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
        SalesforceQueries salesforceLib = new SalesforceQueries();
        String notesQuery = salesforceLib.getNoteDetailsUrl(noteId);

        String notesUrl = salesforceConstants.queryUrlPath() + notesQuery;

        CompositeRequest noteCompositeRequest = new CompositeRequest("GET", notesUrl, "GetNotesList");

        List<CompositeRequest> compositeRequests = new ArrayList<CompositeRequest>();
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

        FormatSalesforceNoteDetails formatSalesforceNoteDetails = new FormatSalesforceNoteDetails();
        GetNoteDetailsFormatterDto noteDetailsFormatterDto = formatSalesforceNoteDetails
                .formatNoteDetails(noteDetailsResponse.getResponseBody(), noteContentResponse.getResponseBody());

        return noteDetailsFormatterDto;

    }

}
