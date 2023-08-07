package com.salessparrow.api.lib.globalConstants.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;

@Component
public class dynamoBeeConfigConstants {
  @Autowired
  private CoreConstants coreConstants;

  public String getChangeLogScanPackage() {
    return "com.salessparrow.api.changelogs";
  }

  public String getChangelogTableName() {
    return coreConstants.environment() +  "_changelog";
  }
}
