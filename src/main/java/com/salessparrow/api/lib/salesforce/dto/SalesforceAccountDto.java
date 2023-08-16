package com.salessparrow.api.lib.salesforce.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.AccountEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceAccountDto {
  private String id;
  private String name;

  public AccountEntity getAccountEntity() {
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setId(this.id);
    accountEntity.setName(this.name);

    return accountEntity;
  }
}
