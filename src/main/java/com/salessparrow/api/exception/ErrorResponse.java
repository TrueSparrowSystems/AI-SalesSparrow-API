package com.salessparrow.api.exception;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.salessparrow.api.lib.ErrorConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorResponse {

    /**
   * Get error response
   * 
   * @param apiIdentifier
   * @param internalErrorIdentifier
   * 
   * @return Map<String, String>
   */
  protected Map<String, String> getErrorResponse(String apiIdentifier, String internalErrorIdentifier, String message) {
    InputStream inputStream = GlobalExceptionHandler.class
        .getResourceAsStream("/com/example/userservice/config/ApiErrorConfig.json");

    ObjectMapper objectMapper = new ObjectMapper();

    TypeReference<HashMap<String, ErrorConfig>> typeReference = new TypeReference<>() {
    };
    Map<String, ErrorConfig> errorDataMap = null;
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

    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("http_code", errorInfo.getHttp_code());
    errorResponse.put("code", errorInfo.getCode());
    errorResponse.put("message", errorInfo.getMessage());
    errorResponse.put("internal_error_identifier", internalErrorIdentifier);
    return errorResponse;
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
