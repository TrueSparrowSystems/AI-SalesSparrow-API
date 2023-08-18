package com.salessparrow.api.dto.requestMapper;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GetAccountsDto {
  @Size(max = 30, message = "search_term_too_long")
  private String q;
}
