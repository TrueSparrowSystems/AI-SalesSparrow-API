package com.salessparrow.api.lib.salesforce.dto;

public class CompositeRequestDto {
  
  private String method;
  private String url;
  private String referenceId;
  private Object body;
  
  public CompositeRequestDto(String method, String url, String referenceId) {
    this.method = method;
    this.url = url;
    this.referenceId = referenceId;
  }

  public CompositeRequestDto(String method, String url, String referenceId, Object body) {
    this.method = method;
    this.url = url;
    this.referenceId = referenceId;
    this.body = body;
  }
  
  public String getMethod() {
    return method;
  }
  
  public String getUrl() {
    return url;
  }
  
  public String getReferenceId() {
    return referenceId;
  }

  public Object getBody() {
    return body;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }

  public void setBody(Object body) {
    this.body = body;
  }
  
}
