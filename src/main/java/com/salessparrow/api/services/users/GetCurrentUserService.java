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
    User user = (User) request.getAttribute("user");

    CurrentUserEntityDto currentUserEntityDto = new CurrentUserEntityDto();
    currentUserEntityDto.setId(user.getExternalUserId());
    currentUserEntityDto.setName(user.getName());
    currentUserEntityDto.setEmail(user.getEmail());

    return currentUserEntityDto;
  }
}