package com.salessparrow.api.dto.formatter;

import lombok.Data;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.AccountEntity;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAccountsFormatterDto {
  private String[] accountIds;
  private Map<String, AccountEntity> accountMapById;
}