package com.salessparrow.api.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.salessparrow.api.dto.formatter.SafeUserDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserProfileService {
  
  public Map<String, Object> getUserProfile(HttpServletRequest request) {
    SafeUserDto safeUser = (SafeUserDto) request.getAttribute("user");

    Map<String, Object> profileResponse = new HashMap<>();
    profileResponse.put("user", safeUser);

    return profileResponse;
  }
}
