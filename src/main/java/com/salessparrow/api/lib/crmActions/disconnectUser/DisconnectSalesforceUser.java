package com.salessparrow.api.lib.crmActions.disconnectUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceTokens;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;
import com.salessparrow.api.repositories.SalesforceUserRepository;

/**
 * DisconnectSalesforceUser class to disconnect a user from Salesforce.
 */
@Component
public class DisconnectSalesforceUser implements DisconnectUser {

  @Autowired
  private SalesforceOauthTokenRepository salesforceOauthTokenRepository;

  @Autowired
  private SalesforceUserRepository salesforceUserRepository;

  @Autowired
  private AwsKms awsKms;

  @Autowired
  private SalesforceTokens salesforceTokens;

  private static final Logger logger = LoggerFactory.getLogger(DisconnectSalesforceUser.class);

  /**
   * Disconnects a user from Salesforce by revoking the tokens and deleting the
   * user data from the database.
   * 
   * @param user
   * @return void
   */
  public void disconnect(User user) {

    String salesforceUserId = user.getExternalUserId();

    logger.info("Disconnecting user from Salesforce: " + salesforceUserId);
    SalesforceOauthToken salesforceOauthToken = salesforceOauthTokenRepository
        .getSalesforceOauthTokenByExternalUserId(salesforceUserId);

    if (salesforceOauthToken == null) {
      throw new CustomException(
          new ErrorObject(
              "l_ca_du_dsu_d_1",
              "something_went_wrong",
              "No tokens data found for this user."));
    }

    String decryptedRefreshToken = awsKms.decryptToken(salesforceOauthToken.getRefreshToken());

    logger.info("Revoking tokens from Salesforce: " + salesforceUserId);
    salesforceTokens.revokeTokens(salesforceOauthToken.getInstanceUrl(), decryptedRefreshToken);

    logger.info("Deleting tokens from database: " + salesforceUserId);
    salesforceOauthTokenRepository.deleteSalesforceOauthTokenBySalesforceOauthToken(salesforceOauthToken);

    logger.info("Deleting user data from database: " + salesforceUserId);
    salesforceUserRepository.removeSalesforceUserData(salesforceUserId);
  }

}
