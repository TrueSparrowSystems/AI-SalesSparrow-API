package com.salessparrow.api.exception;

import java.util.List;

public class CustomException extends RuntimeException {
  private String internalErrorIdentifier;
  private String apiErrorIdentifier;
  private String message;
  private List<String> paramErrorIdentifiers;

  public CustomException(String internalErrorIdentifier, String apiErrorIdentifier, String message) {
    this.internalErrorIdentifier = internalErrorIdentifier;
    this.apiErrorIdentifier = apiErrorIdentifier;
    this.message = message;
  }

  public CustomException(String internalErrorIdentifier, String message, List<String> paramErrorIdentifiers) {
    this.internalErrorIdentifier = internalErrorIdentifier;
    this.message = message;
    this.paramErrorIdentifiers = paramErrorIdentifiers;
  }

  public String getInternalErrorIdentifier() {
    return internalErrorIdentifier;
  }

  public void setInternalErrorIdentifier(String internalErrorIdentifier) {
    this.internalErrorIdentifier = internalErrorIdentifier;
  }

  public String getApiErrorIdentifier() {
    return apiErrorIdentifier;
  }

  public void setApiErrorIdentifier(String apiErrorIdentifier) {
    this.apiErrorIdentifier = apiErrorIdentifier;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<String> getParamErrorIdentifiers() {
    return paramErrorIdentifiers;
  }

  public void setParamErrorIdentifiers(List<String> paramErrorIdentifiers) {
    this.paramErrorIdentifiers = paramErrorIdentifiers;
  }

  
}