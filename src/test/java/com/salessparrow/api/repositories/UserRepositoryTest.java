package com.salessparrow.api.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salessparrow.api.domain.User;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

  @Mock
  private UserRepository userRepository;

  @Test
  public void testFindByEmail() {
    // Mock the UserRepository behavior
    String email = "test@example.com";
    User user = new User();
    user.setId(1L);
    user.setEmail(email);
    user.setPassword("password");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // Call the method to be tested
    Optional<User> foundUser = userRepository.findByEmail(email);

    // Assert the results
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getEmail()).isEqualTo(email);
    assertThat(foundUser.get().getPassword()).isEqualTo("password");
  }

  @Test
  public void testFindByEmail_NotFound() {
    // Mock the UserRepository behavior for a non-existent email
    String email = "nonexistent@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // Call the method to be tested
    Optional<User> foundUser = userRepository.findByEmail(email);

    // Assert the result is empty (user not found)
    assertThat(foundUser).isEmpty();
  }
}
