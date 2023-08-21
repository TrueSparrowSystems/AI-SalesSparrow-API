package com.salessparrow.api.dto.formatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * Redirect url formatter DTO.
 * 
 * @param url
 * 
 * @return RedirectUrlFormatterDto
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RedirectUrlFormatterDto {
  private String url;
}
