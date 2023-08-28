package com.salessparrow.api.dto.requestMapper;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteDto {
  @NotBlank(message = "missing_text")
  @Length(max = 12000, message = "text_too_long")
  private String text;
}



