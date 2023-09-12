package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;
import com.salessparrow.api.config.CoreConstants;

@Component
public class CacheConstants {

	public static final String CACHE_PREFIX;

	static {
		if (CoreConstants.isProductionEnvironment()) {
			CACHE_PREFIX = "prod_";
		}
		else if (CoreConstants.isStagingEnvironment()) {
			CACHE_PREFIX = "stag_";
		}
		else if (CoreConstants.isTestEnvironment()) {
			CACHE_PREFIX = "test_";
		}
		else if (CoreConstants.isLocalTestEnvironment()) {
			CACHE_PREFIX = "ltest_";
		}
		else {
			CACHE_PREFIX = "dev_";
		}
	}

	public static final String SALESFORCE_USER_CACHE = "sf_user";

	public static final Integer SALESFORCE_USER_CACHE_EXP = 30 * 24 * 60 * 60; // 30
																				// days

	public static final String SALESFORCE_OAUTH_TOKEN_CACHE = "sf_oauth_token";

	public static final Integer SALESFORCE_OAUTH_TOKEN_CACHE_EXP = 30 * 24 * 60 * 60; // 30
																						// days

}
