package com.salessparrow.api.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void testGetterAndSetterMethods() {
        // Create a User instance for testing
        User user = new User();

        // Set values using setters
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setCookieToken("token123");
        user.setEncryptionSalt("salt123");
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdatedAt(System.currentTimeMillis());

        // Verify values using getters
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getCookieToken()).isEqualTo("token123");
        assertThat(user.getEncryptionSalt()).isEqualTo("salt123");
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testNoArgsConstructor() {
        User user = new User();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        String email = "test@example.com";
        String password = "password123";
        String cookieToken = "token123";
        String encryptionSalt = "salt123";
        Long createdAt = System.currentTimeMillis();
        Long updatedAt = System.currentTimeMillis();

        User user = new User(email, password, cookieToken, encryptionSalt, createdAt, updatedAt);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getCookieToken()).isEqualTo(cookieToken);
        assertThat(user.getEncryptionSalt()).isEqualTo(encryptionSalt);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }
}
