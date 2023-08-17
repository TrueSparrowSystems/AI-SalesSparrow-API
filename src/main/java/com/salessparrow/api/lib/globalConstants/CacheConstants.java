package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;

@Component
public class CacheConstants {

  public static final String SalesSparrow = "ss";

  public static final String SS_SALESFORCE_USER_CACHE = SalesSparrow + "sf_user";
  public static final Integer SS_SALESFORCE_USER_CACHE_EXP = 30 * 24 * 60 * 60; // 30 days

  public static final String SS_SALESFORCE_OAUTH_TOKEN_CACHE = "sf_oauth_token";
  public static final Integer SS_SALESFORCE_OAUTH_TOKEN_CACHE_EXP = 30 * 24 * 60 * 60; // 30 days
}
