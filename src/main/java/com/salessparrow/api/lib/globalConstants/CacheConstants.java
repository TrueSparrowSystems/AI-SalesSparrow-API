package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;

@Component
public class CacheConstants {

  public static final String SALESFORCE_USER_CACHE = "sf_user";
  public static final Integer SALESFORCE_USER_CACHE_EXP = 30 * 24 * 60 * 60; // 30 days

  public static final String SALESFORCE_ORG_CACHE = "sf_org";
  public static final Integer SALESFORCE_ORG_CACHE_EXP = 30 * 24 * 60 * 60; // 30 days
}
