package com.salessparrow.api.exception;

import com.salessparrow.api.lib.ErrorObject;

public class CustomException extends RuntimeException {
  private ErrorObject errorObject;

  public CustomException(ErrorObject errorObject) {
    this.errorObject = errorObject;
  }

  public ErrorObject getErrorObject() {
    return errorObject;
  }
}