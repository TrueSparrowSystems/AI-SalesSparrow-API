package com.salessparrow.api.exception;

import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;

public class CustomException extends RuntimeException {
  private ErrorObject errorObject;
  private ParamErrorObject paramErrorObject;

  public CustomException(ErrorObject errorObject) {
    this.errorObject = errorObject;
  }

  public CustomException(ParamErrorObject paramErrorObject) {
    this.paramErrorObject = paramErrorObject;
  }

  public ParamErrorObject getParamErrorObject() {
    return paramErrorObject;
  }

  public ErrorObject getErrorObject() {
    return errorObject;
  }
}