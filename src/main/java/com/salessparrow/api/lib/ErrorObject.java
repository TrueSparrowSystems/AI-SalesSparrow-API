package com.salessparrow.api.lib;

public class ErrorObject {

  private String internalErrorIdentifier;
  private String apiErrorIdentifier;
  private String message;

  public ErrorObject() {
  }

  public ErrorObject(String internalErrorIdentifier, String apiErrorIdentifier, String message) {
    this.internalErrorIdentifier = internalErrorIdentifier;
    this.apiErrorIdentifier = apiErrorIdentifier;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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

}
