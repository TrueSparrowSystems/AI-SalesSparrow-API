package com.salessparrow.api.lib.globalConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;

@Component
public class SalesforceConstants {
  @Autowired
  private static CoreConstants coreConstants;

  public String compositeUrlPath() {
    return "/services/data/v58.0/composite";
  }

  public String queryUrlPath() {
    return "/services/data/v58.0/query/?q=";
  }

  public String sObjectsPath() {
    return "/services/data/v58.0/sobjects";
  }

  public String salesforceCompositeUrl() {
    return coreConstants.salesforceClientBaseUrl() + compositeUrlPath();
  }

  public Integer timeoutMillis() {
    return 10000;
  }
}
