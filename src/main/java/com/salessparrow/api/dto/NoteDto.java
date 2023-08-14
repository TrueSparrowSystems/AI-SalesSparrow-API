package com.salessparrow.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteDto {
  @NotBlank(message = "missing_title")
  private String title;

  @NotBlank(message = "missing_content")
  private String content;
}



