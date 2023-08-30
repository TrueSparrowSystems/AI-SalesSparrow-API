package com.salessparrow.api.lib.salesforce.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.lib.salesforce.dto.CompositeResponseDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceErrorObject;

/**
 * SalesforceCompositeResponseHelper is a class for handling the response from the Salesforce API.
 **/
@Component
public class SalesforceCompositeResponseHelper {

    /**
     * Get error object from composite response.
     * 
     * @param responseBody
     * 
     * @return SalesforceErrorObject
     **/
    public SalesforceErrorObject getErrorObjectFromCompositeResponse(JsonNode responseBody) {

        JsonNode CompositeResponse = responseBody.get("compositeResponse");
        SalesforceErrorObject salesforceErrorObject = null;

        for(int i = 0; i < CompositeResponse.size(); i++){
            JsonNode CompositeResponseItem = CompositeResponse.get(i);
            Integer httpStatusCode = CompositeResponseItem.get("httpStatusCode").asInt();
            String referenceId = CompositeResponseItem.get("referenceId").asText();
            
            if(httpStatusCode != 200 && httpStatusCode != 201 && httpStatusCode != 204){

                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                CompositeResponseDto compositeResponseBody = mapper.convertValue(CompositeResponseItem, CompositeResponseDto.class);

                List<CompositeResponseDto.CompositeResponseBody> body = compositeResponseBody.getBody();
                
                for(CompositeResponseDto.CompositeResponseBody bodyItem : body) {
                    String salesforceErrorCode = bodyItem.getErrorCode();
                    String message = bodyItem.getMessage();
                    String errorCode = mapSalesforceErrorCodeToCustomErrorCode(salesforceErrorCode);
    
                    salesforceErrorObject = new SalesforceErrorObject(false, errorCode, message, referenceId);
    
                    return salesforceErrorObject;
                }
            }
        }
        
        return new SalesforceErrorObject(true, null, null, null);
    }

    /**
     * Map Salesforce error code to custom error code.
     * 
     * @param salesforceErrorCode
     * 
     * @return String
     **/
    private String mapSalesforceErrorCodeToCustomErrorCode(String salesforceErrorCode) {
        
        switch(salesforceErrorCode) {
            case "MALFORMED_ID":
            case "INVALID_QUERY_FILTER_OPERATOR":
            case "INVALID_FIELD":
            case "INVALID_CROSS_REFERENCE_KEY":
                return "invalid_params";

            case "NOT_FOUND":
            case "ENTITY_IS_DELETED":
                return "salesforce_resource_not_found";

            case "INSUFFICIENT_ACCESS_OR_READONLY":
            case "INVALID_TYPE":
                return "salesforce_forbidden_api_request";

            default:
                return "something_went_wrong";

        }
    }
}
