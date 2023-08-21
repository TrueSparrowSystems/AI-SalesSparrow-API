package com.salessparrow.api.lib.globalConstants;

/**
 * This class contains the names of the DynamoDB tables used by the application.
 */
public class DynamoDbTableNameConstants {

  private static String environment = System.getenv("ENVIRONMENT");

  /**
   * Returns the name of the table that contains the Salesforce organizations.
   * @return
   */
  public static String salesforceOrganizationsTableName() {
    return environment + "_salesforce_organizations";
  }

  /**
   * Returns the name of the table that contains the Salesforce OAuth tokens.
   * @return
   */
  public static String salesforceOauthTokensTableName() {
    return environment + "_salesforce_oauth_tokens";
  }

  /**
   * Returns the name of the table that contains the Salesforce users.
   * @return
   */
  public static String salesforceUsersTableName() {
    return environment + "_salesforce_users";
  }
}
