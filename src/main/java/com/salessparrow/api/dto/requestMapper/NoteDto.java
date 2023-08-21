package com.salessparrow.api.dto.requestMapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteDto {
  @NotBlank(message = "missing_text")
  private String text;
}



