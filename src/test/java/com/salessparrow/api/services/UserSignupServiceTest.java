package com.salessparrow.api.services;

import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.salessparrow.api.config.CoreConstants;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.UserDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.lib.LocalCipher;
import com.salessparrow.api.repositories.UserRepository;

public class UserSignupServiceTest {

    @InjectMocks
    private UserSignupService userSignupService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocalCipher localCipher;

    @Mock
    private CoreConstants coreConstants;

    @Mock
    private CookieHelper cookieHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String decryptedSalt = "decrypted_salt";
        String encryptedSalt = "encrypted_salt";
        String encryptedPassword = "encrypted_password";
        String encryptedCookieToken = "encrypted_cookie_token";
        String cookieToken = "cookie_token";
        String userLoginCookieValue = "user_login_cookie_value";
        Long currentTime = System.currentTimeMillis();

        UserDto userDto = new UserDto(email, password);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(localCipher.generateRandomSalt()).thenReturn(decryptedSalt);
        Mockito.when(localCipher.generateRandomIv(32)).thenReturn(cookieToken);
        Mockito.when(coreConstants.encryptionKey()).thenReturn("encryption_key");
        Mockito.when(localCipher.encrypt("encryption_key", decryptedSalt)).thenReturn(encryptedSalt);
        Mockito.when(localCipher.encrypt(decryptedSalt, password)).thenReturn(encryptedPassword);
        Mockito.when(localCipher.encrypt(decryptedSalt, cookieToken)).thenReturn(encryptedCookieToken);

        User savedUser = new User(email, encryptedPassword, encryptedCookieToken, encryptedSalt, currentTime, currentTime);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        Mockito.when(cookieHelper.getCookieValue(savedUser, decryptedSalt, currentTime)).thenReturn(userLoginCookieValue);

        // Act
        Map<String, Object> result = userSignupService.createUser(userDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsKey("user"));
        Assertions.assertTrue(result.containsKey("cookie"));

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(localCipher, Mockito.times(1)).generateRandomSalt();
        Mockito.verify(localCipher, Mockito.times(1)).generateRandomIv(32);
        Mockito.verify(coreConstants, Mockito.times(1)).encryptionKey();
        Mockito.verify(localCipher, Mockito.times(1)).encrypt("encryption_key", decryptedSalt);
        Mockito.verify(localCipher, Mockito.times(1)).encrypt(decryptedSalt, password);
        Mockito.verify(localCipher, Mockito.times(1)).encrypt(decryptedSalt, cookieToken);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        UserDto userDto = new UserDto(email, password);
        User existingUser = new User();
        existingUser.setEmail(email);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        Assertions.assertThrows(CustomException.class, () -> userSignupService.createUser(userDto));

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(localCipher, Mockito.times(0)).generateRandomSalt();
        Mockito.verify(localCipher, Mockito.times(0)).generateRandomIv(Mockito.anyInt());
        Mockito.verify(coreConstants, Mockito.times(0)).encryptionKey();
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }
}
