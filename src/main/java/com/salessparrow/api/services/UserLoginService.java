package com.salessparrow.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.ErrorObject;
import com.salessparrow.api.lib.LocalCipher;
import com.salessparrow.api.repositories.UserRepository;
import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.UserDto;

import java.util.Optional;

@Service
public class UserLoginService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CoreConstants coreConstants;

  @Autowired
  private LocalCipher localCipher;

  @Autowired
  private CookieHelper cookiHelper;

  Logger logger = LoggerFactory.getLogger(UserLoginService.class);

  public UserLoginServiceDto authenticateUser(UserDto userDto) {
    logger.info("User login service called");

    Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
    if (!optionalUser.isPresent()) {
      throw new CustomException(
          new ErrorObject(
              "s_uls_au_1",
              "invalid_credentials",
              "User does not exist"));
    }

    User user = optionalUser.get();
    String decryptedSalt = localCipher.decrypt(coreConstants.encryptionKey(), user.getEncryptionSalt());
    String encryptedPassword = localCipher.encrypt(decryptedSalt, userDto.getPassword());

    if (!encryptedPassword.equals(user.getPassword())) {
      throw new CustomException(
          new ErrorObject(
              "s_uls_au_3",
              "invalid_credentials",
              "Invalid password"));
    }

    Long currentTime = System.currentTimeMillis();
    String userLoginCookieValue = cookiHelper.getCookieValue(user, decryptedSalt, currentTime);

    UserLoginServiceDto userLoginServiceDto = new UserLoginServiceDto();

    userLoginServiceDto.setUser(user);
    userLoginServiceDto.setCookie(userLoginCookieValue);

    return userLoginServiceDto;
  }

  public static class UserLoginServiceDto {
    private User user;
    private String cookie;

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }

    public String getCookie() {
      return cookie;
    }

    public void setCookie(String cookie) {
      this.cookie = cookie;
    }
  }
}
