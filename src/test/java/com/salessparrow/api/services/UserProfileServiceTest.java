package com.salessparrow.api.services;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.salessparrow.api.dto.formatter.SafeUserDto;
import com.salessparrow.api.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

public class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserProfile() {
        // Arrange
        Long id = 1L;
        String email = "test@example.com";
        long createdAt = System.currentTimeMillis();
        long updatedAt = System.currentTimeMillis() + 1000; // Assuming updatedAt is one second later than createdAt

        SafeUserDto safeUserDto = new SafeUserDto();
        safeUserDto.setId(id);
        safeUserDto.setEmail(email);
        safeUserDto.setCreatedAt(createdAt);
        safeUserDto.setUpdatedAt(updatedAt);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getAttribute("user")).thenReturn(safeUserDto);

        // Act
        Map<String, Object> result = userProfileService.getUserProfile(request);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsKey("user"));

        SafeUserDto resultUser = (SafeUserDto) result.get("user");
        Assertions.assertEquals(id, resultUser.getId());
        Assertions.assertEquals(email, resultUser.getEmail());
        Assertions.assertEquals(createdAt, resultUser.getCreatedAt());
        Assertions.assertEquals(updatedAt, resultUser.getUpdatedAt());
    }

}
