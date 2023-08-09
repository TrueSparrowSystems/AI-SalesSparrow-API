package com.salessparrow.api.lib.globalConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;

@Component
public class SalesforceConstants {

  @Autowired
  public SalesforceConstants(CoreConstants coreConstants) {
    this.coreConstants = coreConstants;
  }

  public CoreConstants coreConstants;

  public String compositeUrlPath() {
    return "/services/data/v58.0/composite";
  }

  public String queryUrlPath() {
    return "/services/data/v58.0/query/?q=";
  }

  public String sObjectsPath() {
    return "/services/data/v58.0/sobjects";
  }

  public String salesforceCompositeUrl(String urlPrefix) {
    return urlPrefix + compositeUrlPath();
  }

  public String oauth2AuthorizeUrl() {
    return coreConstants.salesforceAuthUrl() + "/services/oauth2/authorize";
  }

  public String oauth2Url() {
    return coreConstants.salesforceAuthUrl() + "/services/oauth2/token";
  }

  public String identityUrl() {
    return "/services/oauth2/userinfo";
  }

  public String authorizationCodeGrantType() {
    return "authorization_code";
  }

  public String refreshTokenGrantType() {
    return "refresh_token";
  }

  public Integer timeoutMillis() {
    return 10000;
  }
}
