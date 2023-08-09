package com.salessparrow.api.lib.crmActions.getNoteDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.crmActions.getNotesList.GetSalesforceNotesList;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.helper.SalesforceOAuthRequest;
import com.salessparrow.api.lib.helper.SalesforceOAuthRequestInterface;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceNoteDetails;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

@Component
public class GetSalesforceNoteDeatails implements GetNoteDetails{

    @Autowired
    private GetSalesforceNotesList getSalesforceNotesList;

    @Autowired
    private SalesforceConstants salesforceConstants;

    @Autowired
    private SalesforceOauthTokenRepository sfOauthTokenRepository;

    @Autowired
    private SalesforceOAuthRequest salesforceOauthRequest;

    /**
     * Get the content of a note
     *  
     * @param noteid
     * @param salesforceUserId
     * 
     * @return HttpClient.HttpResponse
     */
    public HttpClient.HttpResponse getContent(String noteid, String salesforceUserId){

        SalesforceOauthToken sfOAuthToken = sfOauthTokenRepository.getSalesforceOauthTokenBySalesforceUserId(salesforceUserId);

        String noteContentQuery = salesforceConstants.salesfroceContentUrl(sfOAuthToken.getInstanceUrl(), noteid);

        Integer timeoutMillis = salesforceConstants.timeoutMillis();

        SalesforceOAuthRequestInterface<HttpClient.HttpResponse> request = token -> {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        HttpClient.HttpResponse response = HttpClient.makeGetRequest(
            noteContentQuery, 
            headers, 
            timeoutMillis);

        return response;
        };

        HttpClient.HttpResponse response = null;
        try {
        response = salesforceOauthRequest.makeRequest(salesforceUserId, request);
        } catch (Exception e) {
        throw new CustomException(
            new ErrorObject(
            "s_l_ca_gnd_gsnd_1",
            "something_went_wrong",
            e.getMessage()));
        }
        return response;
    
    }

    /**
     * Get the details of a note
     * @param user
     * @param noteid
     * 
     * @return GetNoteDetailsFormatterDto
     **/
    public GetNoteDetailsFormatterDto getNoteDetails(SalesforceUser user, String noteid){

        String salesforceUserId = user.getExternalUserId();

        List<String> noteIds = new ArrayList<String>(
            Arrays.asList(noteid)
        );

        HttpClient.HttpResponse noteDetailsResponse = getSalesforceNotesList.getNotes(noteIds, salesforceUserId);

        HttpClient.HttpResponse noteContentResponse = getContent(noteid, salesforceUserId);

        FormatSalesforceNoteDetails formatSalesforceNoteDetails = new FormatSalesforceNoteDetails();
        GetNoteDetailsFormatterDto noteDetailsFormatterDto = formatSalesforceNoteDetails.formatNoteDetails(noteDetailsResponse.getResponseBody(), noteContentResponse.getResponseBody());

        return noteDetailsFormatterDto;
        
    }

}
