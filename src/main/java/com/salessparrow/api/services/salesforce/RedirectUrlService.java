package com.salessparrow.api.services.salesforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.dto.SalesforceRedirectUrlDto;

@Service
public class RedirectUrlService {

  @Autowired
  private CoreConstants coreConstants;

  Logger logger = LoggerFactory.getLogger(RedirectUrlService.class);

  public RedirectUrlServiceDto getSalesforceOauthUrl(SalesforceRedirectUrlDto salesforceRedirectUrlDto) {

    String redirectUri = salesforceRedirectUrlDto.getRedirectUri();
    String state = salesforceRedirectUrlDto.getState();

    String salesforceLoginUrl = coreConstants.salesforceAuthUrl();
    String salesforceClientId = coreConstants.salesforceClientId();

    String salesforceOauthUrl = salesforceLoginUrl + "?response_type=code" +
        "&client_id=" + salesforceClientId +
        "&redirect_uri=" + redirectUri;

    if (state != null && !state.isEmpty()) {
      salesforceOauthUrl += "&state=" + state;
    }

    RedirectUrlServiceDto salesforceOauthServiceDto = new RedirectUrlServiceDto();
    salesforceOauthServiceDto.setUrl(salesforceOauthUrl);

    return salesforceOauthServiceDto;
  }

  public static class RedirectUrlServiceDto {
    private String url;

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}