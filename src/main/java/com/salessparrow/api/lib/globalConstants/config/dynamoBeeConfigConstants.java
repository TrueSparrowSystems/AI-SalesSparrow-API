package com.salessparrow.api.lib.globalConstants.config;

import org.springframework.stereotype.Component;
import com.salessparrow.api.config.CoreConstants;

@Component
public class dynamoBeeConfigConstants {

	public String getChangeLogScanPackage() {
		return "com.salessparrow.api.changelogs";
	}

	public String getChangelogTableName() {
		return CoreConstants.environment() + "_changelog";
	}

	public String authorName() {
		return CoreConstants.environment() + "_salessparrow_api";
	}

}
