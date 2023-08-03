package com.salessparrow.api.lib.salesforce;

public class CompositeRequest {
  
  private String method;
  private String url;
  private String referenceId;
  
  public CompositeRequest(String method, String url, String referenceId) {
    this.method = method;
    this.url = url;
    this.referenceId = referenceId;
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
  
  public void setMethod(String method) {
    this.method = method;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }
  
}
