package com.salessparrow.api.lib.salesforce.wrappers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequestInterface;

/**
 * Wrapper class for Get the content of a note
 */
@Component
public class SalesforceGetNoteContent {
    @Autowired
    private SalesforceConstants salesforceConstants;

    @Autowired
    private SalesforceRequest salesforceOauthRequest;
    
    /**
     * Get the content of a note
     * 
     * @param noteId
     * @param salesforceUserId
     * 
     * @return HttpResponse
     */
    public HttpClient.HttpResponse getNoteContent(String noteId, String salesforceUserId){
        Integer timeoutMillis = salesforceConstants.timeoutMillis();

        SalesforceRequestInterface<HttpClient.HttpResponse> request = (token, instanceUrl) -> {
            String noteContentQuery = salesforceConstants.salesfroceContentUrl(instanceUrl, noteId);
          
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);

            HttpClient.HttpResponse response = HttpClient.makeGetRequest(
                    noteContentQuery,
                    headers,
                    timeoutMillis);

            return response;
        };

        HttpClient.HttpResponse response = salesforceOauthRequest.makeRequest(salesforceUserId, request);
        return response;
    }
}
