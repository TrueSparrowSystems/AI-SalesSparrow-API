package com.salessparrow.api.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ErrorResponseObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;

@ControllerAdvice
public class GlobalExceptionHandler {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Autowired
  private ErrorResponse er;

  /**
   * Handle 404. Catches the exception for undefined endpoints 
   * 
   * @param NoHandlerFoundException
   * 
   * @return ResponseEntity<ErrorResponseObject>
   */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseObject> handleNoHandlerFoundException(NoHandlerFoundException ex) {
      ErrorResponseObject errorResponse = null;

      errorResponse = er.getErrorResponse(
        "resource_not_found",
          "a_e_geh_nf_1", 
          "handleNoHandlerFoundException");

      return ResponseEntity.status(errorResponse.getHttpCode())
        .body(errorResponse);
    }


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

    if(ex.getParamErrorObject() != null){
      ParamErrorObject paramErrorObject = ex.getParamErrorObject();
      errorResponse = er.getParamErrorResponse(
          paramErrorObject.getInternalErrorIdentifier(), 
          paramErrorObject.getMessage(),
          paramErrorObject.getParamErrorIdentifiers());
    }
    else if (ex.getErrorObject() == null || ex.getErrorObject().getApiErrorIdentifier() == null) {
      errorResponse = er.getErrorResponse(
        "something_went_wrong",
          "e_geh_hce_1", 
          ex.getMessage());
    } else {
      ErrorObject errorObject = ex.getErrorObject();
      errorResponse = er.getErrorResponse(
          errorObject.getApiErrorIdentifier(),
          errorObject.getInternalErrorIdentifier(), 
          errorObject.getMessage());
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
   * @return ResponseEntity<ErrorResponseObject>
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponseObject> handleRuntimeException(RuntimeException ex) {
    ex.printStackTrace();

    ErrorResponseObject errorResponse = er.getErrorResponse("something_went_wrong",
        "e_geh_hre_1", ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponseObject> handleValidationException(MethodArgumentNotValidException ex) {
    List<String> paramErrorIdentifiers = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      paramErrorIdentifiers.add(error.getDefaultMessage());
    }
    
    CustomException ce2 = new CustomException(
        new ParamErrorObject(
            "b_2",
            paramErrorIdentifiers.toString(),
            paramErrorIdentifiers));

    return handleCustomException(ce2);
  }
}