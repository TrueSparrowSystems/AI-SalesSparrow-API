package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;

@Component
public class CookieConstants {

	public static final String LATEST_VERSION = "1";

	public static final String USER_LOGIN_COOKIE_NAME = "ulcn";

	public static final Integer USER_LOGIN_COOKIE_EXPIRY_IN_SEC = 30 * 60 * 60 * 24;

}
