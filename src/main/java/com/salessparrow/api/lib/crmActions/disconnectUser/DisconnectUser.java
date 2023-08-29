package com.salessparrow.api.lib.crmActions.disconnectUser;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;

/**
 * DisconnectUser interface for disconnecting a user from the CRM.
 */
@Component
public interface DisconnectUser {

  public void disconnect(User User);

}
