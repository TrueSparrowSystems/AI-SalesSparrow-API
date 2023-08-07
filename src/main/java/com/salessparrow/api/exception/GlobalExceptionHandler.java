package com.salessparrow.api.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salessparrow.api.lib.errorLib.ErrorResponseObject;

@ControllerAdvice
public class GlobalExceptionHandler {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Autowired
  private ErrorResponse er;

  /**
   * Handle custom exception
   * 
   * @param ex
   * 
   * @return ResponseEntity<Map<String, String>>
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponseObject> handleCustomException(CustomException ex) {
    ErrorResponseObject errorResponse = null;

    if(ex != null && ex.getApiErrorIdentifier() != null) {
      errorResponse = er.getErrorResponse(
        ex.getApiErrorIdentifier(),
        ex.getInternalErrorIdentifier(), 
        ex.getMessage());
    }
    else if(ex != null && ex.getParamErrorIdentifiers() != null){
      errorResponse = er.getParamErrorResponse(
        ex.getInternalErrorIdentifier(), 
        ex.getMessage(), 
        ex.getParamErrorIdentifiers());
    }   
    else{
      errorResponse = er.getErrorResponse(
        "something_went_wrong",
          "b_1", 
          ex.getMessage());
    }

    logger.error("Error response: {}", errorResponse);

    return ResponseEntity.status(errorResponse.getHttpCode())
        .body(errorResponse);
  }

  /**
   * Catch-all exception handler for any unhandled runtime exception
   * 
   * @param ex
   * 
   * @return ResponseEntity<Map<String, String>>
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponseObject> handleRuntimeException(RuntimeException ex) {
    ex.printStackTrace();

    ErrorResponseObject errorResponse = er.getErrorResponse("something_went_wrong",
        "b_1", ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponseObject> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errorMap = new HashMap<>();

    logger.info("null pointer exception");

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errorMap.put(error.getField(), error.getDefaultMessage());
    }

    CustomException ce = new CustomException(
            "b_2",
            "invalid_params",
            errorMap.toString());

    return handleCustomException(ce);
  }
}