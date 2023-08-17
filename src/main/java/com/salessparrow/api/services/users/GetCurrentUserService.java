package com.salessparrow.api.services.users;

import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.CurrentUserEntityDto;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Get current user service.
 */
@Service
public class GetCurrentUserService {

  public CurrentUserEntityDto getCurrentUser(HttpServletRequest request) {
    User currentUser = (User) request.getAttribute("current_user");

    CurrentUserEntityDto currentUserEntityDto = new CurrentUserEntityDto();
    currentUserEntityDto.setId(currentUser.getExternalUserId());
    currentUserEntityDto.setName(currentUser.getName());
    currentUserEntityDto.setEmail(currentUser.getEmail());

    return currentUserEntityDto;
  }
}