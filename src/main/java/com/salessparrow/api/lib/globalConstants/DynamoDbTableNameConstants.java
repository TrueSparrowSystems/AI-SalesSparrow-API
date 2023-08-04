package com.salessparrow.api.lib.globalConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.config.CoreConstants;

@Component
public class DynamoDbTableNameConstants {
  
  @Autowired
  private CoreConstants coreConstants;

  public String salesforceOrganizationsTableName() {
    return coreConstants.tableNamePrefix() + "salesforce_organizations";
  }

  public String salesforceOauthTokensTableName() {
    return coreConstants.tableNamePrefix() + "salesforce_oauth_tokens";
  }

  public String salesforceUsersTableName() {
    return coreConstants.tableNamePrefix() + "salesforce_users";
  }
}
