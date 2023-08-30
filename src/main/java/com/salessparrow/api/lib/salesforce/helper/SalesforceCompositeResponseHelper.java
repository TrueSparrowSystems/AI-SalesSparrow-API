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

        SalesforceErrorObject salesforceErrorObject = new SalesforceErrorObject(true, null, null, null);

        JsonNode CompositeResponse = responseBody.get("compositeResponse");
        boolean isCompositeResponseSuccess = true;
        for(int i = 0; i < CompositeResponse.size(); i++){
            JsonNode CompositeResponseItem = CompositeResponse.get(i);
            Integer httpStatusCode = CompositeResponseItem.get("httpStatusCode").asInt();

            if(httpStatusCode != 200 && httpStatusCode != 201 && httpStatusCode != 204){
                isCompositeResponseSuccess = false;
            }
        }

        if(isCompositeResponseSuccess){
            return salesforceErrorObject;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        CompositeResponseDto compositeResponse = mapper.convertValue(responseBody, CompositeResponseDto.class);


        List<CompositeResponseDto.compositeResponse> compositeResponseList = compositeResponse.getCompositeResponse();

        for(CompositeResponseDto.compositeResponse compositeResponseItem : compositeResponseList) {
            List<CompositeResponseDto.Body> body = compositeResponseItem.getBody();
            int httpStatusCode = compositeResponseItem.getHttpStatusCode();
            String referenceId = compositeResponseItem.getReferenceId();

            if(httpStatusCode != 200 && httpStatusCode != 201 && httpStatusCode != 204) {
        
                for(CompositeResponseDto.Body bodyItem : body) {
                    String salesforceErrorCode = bodyItem.getErrorCode();
                    String message = bodyItem.getMessage();
                    String errorCode = "";
    
                    if(salesforceErrorCode.equals("MALFORMED_ID")){
                        errorCode = "invalid_params";
                    }
                    else if(salesforceErrorCode.equals("NOT_FOUND")){
                        errorCode = "salesforce_resource_not_found";
                    }
                    else if(salesforceErrorCode.equals("ENTITY_IS_DELETED")){
                        errorCode = "salesforce_resource_not_found";
                    }
                    else if(salesforceErrorCode.equals("INSUFFICIENT_ACCESS_OR_READONLY")){
                        errorCode = "salesforce_forbidden_api_request";
                    }
                    else if(salesforceErrorCode.equals("INVALID_TYPE")){
                        errorCode = "salesforce_forbidden_api_request";
                    }
                    else if(salesforceErrorCode.equals("INVALID_QUERY_FILTER_OPERATOR")){
                        errorCode = "invalid_params";
                    }
                    else if(salesforceErrorCode.equals("INVALID_FIELD")){
                        errorCode = "invalid_params";
                    }
                    else if(salesforceErrorCode.equals("INVALID_CROSS_REFERENCE_KEY")){
                        errorCode = "invalid_params";
                    }
                    else{
                        errorCode = "something_went_wrong";
                    }
    
                    salesforceErrorObject = new SalesforceErrorObject(false, errorCode, message, referenceId);
    
                    return salesforceErrorObject;
                }
            }
        }

        return salesforceErrorObject;
    }
}
