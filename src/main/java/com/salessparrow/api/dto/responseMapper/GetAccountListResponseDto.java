package com.salessparrow.api.dto.responseMapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.AccountEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAccountListResponseDto {

  private List<String> accountIds;
  private Map<String, AccountEntity> accountMapById;
}
