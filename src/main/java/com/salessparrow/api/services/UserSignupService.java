package com.salessparrow.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.controllers.UserController;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.UserDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.ErrorObject;
import com.salessparrow.api.lib.LocalCipher;
import com.salessparrow.api.repositories.UserRepository;

@Service
public class UserSignupService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LocalCipher localCipher;

  @Autowired
  private CoreConstants coreConstants;

  @Autowired
  private CookieHelper cookieHelper;


  Logger logger = LoggerFactory.getLogger(UserController.class);

  public Map<String, Object> createUser(UserDto userDto) {

    Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
    if (optionalUser.isPresent()) {
      throw new CustomException(
        new ErrorObject(
          "s_uss_cu_1",
          "user_already_exists",
          "User already exists"));
    }

    String decryptedSalt = localCipher.generateRandomSalt();
    String cookieToken = localCipher.generateRandomIv(32);

    String encryptedSalt = localCipher.encrypt(coreConstants.encryptionKey(), decryptedSalt);
    String encryptedPassword = localCipher.encrypt(decryptedSalt, userDto.getPassword());
    String encryptedCookieToken = localCipher.encrypt(decryptedSalt, cookieToken);

    Long currentTime = System.currentTimeMillis();

    User user = new User(
        userDto.getEmail(),
        encryptedPassword,
        encryptedCookieToken,
        encryptedSalt,
        currentTime,
        currentTime);

    userRepository.save(user);

    String userLoginCookieValue = cookieHelper.getCookieValue(user, decryptedSalt, currentTime);

    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("user", user);
    resultMap.put("cookie", userLoginCookieValue);

    return resultMap;
  }

}
