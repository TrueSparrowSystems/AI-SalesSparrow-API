package com.salessparrow.api.services.users;

import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CurrentUserEntityDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class GetCurrentUserService {
    
  public CurrentUserEntityDto getCurrentUser(HttpServletRequest request) {
    SalesforceUser user = (SalesforceUser) request.getAttribute("user");

    CurrentUserEntityDto currentUserEntityDto = new CurrentUserEntityDto();
    currentUserEntityDto.setId(user.getId());
    currentUserEntityDto.setName(user.getName());
    currentUserEntityDto.setEmail(user.getEmail());

    return currentUserEntityDto;
  }
}