package com.salessparrow.api.dto.requestMapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonNaming(LowerCamelCaseStrategy.class)
public class GetAccountsFeedDto {

  @Pattern(regexp = "^[A-Za-z0-9+/]*={0,2}$", message = "invalid_pagination_identifier")
  private String pagination_identifier;
}
