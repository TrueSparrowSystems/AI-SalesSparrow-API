package com.salessparrow.api.services;

import java.util.Optional;

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
import com.salessparrow.api.services.UserLoginService.UserLoginServiceDto;

public class UserLoginServiceTest {

    @InjectMocks
    private UserLoginService userLoginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoreConstants coreConstants;

    @Mock
    private LocalCipher localCipher;

    @Mock
    private CookieHelper cookieHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        UserDto userDto = new UserDto(email, password);
        User user = new User();
        user.setEmail(email);
        String encryptedPassword = "encrypted_password";
        user.setPassword(encryptedPassword);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(coreConstants.encryptionKey()).thenReturn("encryption_key");
        Mockito.when(localCipher.decrypt("encryption_key", user.getEncryptionSalt())).thenReturn("decrypted_salt");
        Mockito.when(localCipher.encrypt("decrypted_salt", password)).thenReturn(encryptedPassword);
        Mockito.when(cookieHelper.getCookieValue(user, "decrypted_salt", System.currentTimeMillis()))
                .thenReturn("cookie_value");
        // Act
        UserLoginServiceDto result = userLoginService.authenticateUser(userDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result.getUser());

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(coreConstants, Mockito.times(1)).encryptionKey();
        Mockito.verify(localCipher, Mockito.times(1)).decrypt("encryption_key", user.getEncryptionSalt());
        Mockito.verify(localCipher, Mockito.times(1)).encrypt("decrypted_salt", password);
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        UserDto userDto = new UserDto(email, password);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(CustomException.class, () -> userLoginService.authenticateUser(userDto));

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
    }

    @Test
    void testAuthenticateUser_WrongPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "wrong_password";

        UserDto userDto = new UserDto(email, password);
        User user = new User();
        user.setEmail(email);
        String encryptedPassword = "encrypted_password";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(coreConstants.encryptionKey()).thenReturn("encryption_key");
        Mockito.when(localCipher.decrypt("encryption_key", user.getEncryptionSalt())).thenReturn("decrypted_salt");
        Mockito.when(localCipher.encrypt("decrypted_salt", password)).thenReturn(encryptedPassword);

        // Act & Assert
        Assertions.assertThrows(CustomException.class, () -> userLoginService.authenticateUser(userDto));

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(coreConstants, Mockito.times(1)).encryptionKey();
        Mockito.verify(localCipher, Mockito.times(1)).decrypt("encryption_key", user.getEncryptionSalt());
        Mockito.verify(localCipher, Mockito.times(1)).encrypt("decrypted_salt", password);
        Mockito.verify(cookieHelper, Mockito.times(0)).getCookieValue(Mockito.any(), Mockito.any(), Mockito.anyLong());
    }

}
