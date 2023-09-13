package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;
import com.salessparrow.api.config.CoreConstants;

@Component
public class CacheConstants {

	public static final String CACHE_SUFFIX = getCacheSuffix();

	public static final String SALESFORCE_USER_CACHE = "sf_user";

	public static final Integer SALESFORCE_USER_CACHE_EXP = 30 * 24 * 60 * 60; // 30
																				// days

	public static final String SALESFORCE_OAUTH_TOKEN_CACHE = "sf_oauth_token";

	public static final Integer SALESFORCE_OAUTH_TOKEN_CACHE_EXP = 30 * 24 * 60 * 60; // 30
																						// days

	public static String getCacheSuffix() {
		if (CoreConstants.isProductionEnvironment()) {
			return "_prod";
		}
		else if (CoreConstants.isStagingEnvironment()) {
			return "_stag";
		}
		else if (CoreConstants.isTestEnvironment()) {
			return "_test";
		}
		else if (CoreConstants.isLocalTestEnvironment()) {
			return "_ltest";
		}
		else {
			return "_dev";
		}
	}

}
