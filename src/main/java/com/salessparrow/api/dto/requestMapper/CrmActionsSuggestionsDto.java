package com.salessparrow.api.dto.requestMapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * CrmActionsSuggestionsDto is a dto class for the crm actions suggestions.
 */
@Data
public class CrmActionsSuggestionsDto {
  @NotBlank(message = "missing_text")
  private String text;
}
