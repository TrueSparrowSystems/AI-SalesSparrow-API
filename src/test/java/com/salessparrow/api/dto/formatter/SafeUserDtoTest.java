package com.salessparrow.api.dto.formatter;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SafeUserDtoTest {
  @Test
  void testGetAndSetCreatedAt() {
    SafeUserDto safeUserDto = new SafeUserDto();

    long createdAt = System.currentTimeMillis();
    safeUserDto.setCreatedAt(createdAt);

    assertThat(safeUserDto.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void testGetAndSetEmail() {
    SafeUserDto safeUserDto = new SafeUserDto();

    String email = "test@email.com";
    safeUserDto.setEmail(email);

    assertThat(safeUserDto.getEmail()).isEqualTo(email);
  }

  @Test
  void testGetAndSetId() {
    SafeUserDto safeUserDto = new SafeUserDto();
    safeUserDto.setId(1L);

    assertThat(safeUserDto.getId()).isEqualTo(1L);
  }

  @Test
  void testGetAndSetUpdatedAt() {
    SafeUserDto safeUserDto = new SafeUserDto();

    long updatedAt = System.currentTimeMillis();
    safeUserDto.setUpdatedAt(updatedAt);

    assertThat(safeUserDto.getUpdatedAt()).isEqualTo(updatedAt);
  }
}
