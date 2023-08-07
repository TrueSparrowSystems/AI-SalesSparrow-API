package com.salessparrow.api.exception;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Component;

import com.salessparrow.api.lib.errorLib.ErrorConfig;
import com.salessparrow.api.lib.errorLib.ErrorResponseObject;
import com.salessparrow.api.lib.errorLib.ParamErrorConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ErrorResponse {

  /**
   * Get error response
   * 
   * @param apiIdentifier
   * @param internalErrorIdentifier
   * 
   * @return Map<String, String>
   */
  protected ErrorResponseObject getErrorResponse(String apiIdentifier, String internalErrorIdentifier, String message) {
    InputStream inputStream = GlobalExceptionHandler.class
        .getResourceAsStream("/com/salessparrow/api/config/ApiErrorConfig.json");

    ObjectMapper objectMapper = new ObjectMapper();

    TypeReference<HashMap<String, ErrorConfig>> typeReference = new TypeReference<>() {
    };
    Map<String, ErrorConfig> errorDataMap = new HashMap<>();
    try {
      errorDataMap = objectMapper.readValue(inputStream, typeReference);
    } catch (Exception e) {
      e.printStackTrace();
    }

    ErrorConfig errorInfo = errorDataMap.get(apiIdentifier);

    if (errorInfo == null) {
      errorInfo = errorDataMap.get("something_went_wrong");
    }

    logError(message, Integer.parseInt(errorInfo.getHttp_code()), internalErrorIdentifier);
    

    ErrorResponseObject errorResponseObject = new ErrorResponseObject(
        Integer.parseInt(errorInfo.getHttp_code()), 
        errorInfo.getCode(), 
        errorInfo.getMessage(),
        internalErrorIdentifier,
        new ArrayList<ParamErrorConfig>());

    return errorResponseObject;
  }

  protected ErrorResponseObject getParamErrorResponse(String internalErrorIdentifier, String message,
      List<String> params) {
    InputStream inputStream = GlobalExceptionHandler.class
        .getResourceAsStream("/com/salessparrow/api/config/ParamErrorConfig.json");

    ObjectMapper objectMapper = new ObjectMapper();

    TypeReference<HashMap<String, ParamErrorConfig>> typeReference = new TypeReference<>() {
    };
    Map<String, ParamErrorConfig> paramErrorDataMap = new HashMap<>();
    try {
      paramErrorDataMap = objectMapper.readValue(inputStream, typeReference);
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<ParamErrorConfig> paramErrorConfigList = new ArrayList<ParamErrorConfig>();
    if(params.size() > 0){
      for(String param : params){
        ParamErrorConfig paramErrorConfig = paramErrorDataMap.get(param);
        if(paramErrorConfig != null){
          paramErrorConfigList.add(paramErrorConfig);
        }
      }
    }

    logError(message,400, internalErrorIdentifier);

    ErrorResponseObject errorResponseObject = new ErrorResponseObject(
        400, 
        "At least one parameter is invalid or missing.", 
        "INVALID_PARAMS",
        internalErrorIdentifier,
        paramErrorConfigList);

    return errorResponseObject;
  }


  /**
   * Log error
   * 
   * @param message
   * @param httpCode
   * @param internal_error_identifier
   * 
   * @return void
   */
  private void logError(String message, int httpCode, String internal_error_identifier) {
    System.out.println("Error message: " + message);
    System.out.println("Error code: " + httpCode);
    System.out.println("Internal error identifier: " + internal_error_identifier);
  }

}
