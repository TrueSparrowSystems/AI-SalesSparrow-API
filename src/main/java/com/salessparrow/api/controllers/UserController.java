package com.salessparrow.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.GetCurrentUserFormatterDto;
import com.salessparrow.api.services.users.GetCurrentUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  
  @Autowired
  private GetCurrentUserService getCurrentUserService = new GetCurrentUserService();
  
  @GetMapping("/current")
  public ResponseEntity<GetCurrentUserFormatterDto> GetCurrentUser(HttpServletRequest request) {
  
    GetCurrentUserFormatterDto getCurrentUserFormatterDto = new GetCurrentUserFormatterDto();
    getCurrentUserFormatterDto.setCurrent_user(getCurrentUserService.getCurrentUser(request));

    return ResponseEntity.ok().body(getCurrentUserFormatterDto);
  }
}
