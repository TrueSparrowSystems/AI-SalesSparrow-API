package com.salessparrow.api.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.crmActions.disconnectUser.DisconnectUserFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service for disconnecting a user from the CRM.
 */
@Service
public class DisconnectUserService {

  @Autowired
  private DisconnectUserFactory disconnectUserFactory;

  /**
   * Disconnect a user from the CRM.
   * 
   * @param request
   * 
   * @return void
   */
  public void disconnect(HttpServletRequest request) {
    User currentUser = (User) request.getAttribute("current_user");
    disconnectUserFactory.disconnect(currentUser);
  }

}
