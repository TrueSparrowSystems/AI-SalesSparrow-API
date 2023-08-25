package com.salessparrow.api.lib.salesforce.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.base.CaseFormat;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.entities.ContactEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceAccountDto {
  private String id;
  private String name;
  private Map<String, Object> additionalFields = new HashMap<>();
  private ContactsWrapper Contacts;

  public AccountEntity getAccountEntity() {
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setId(this.id);
    accountEntity.setName(this.name);
    accountEntity.setAdditionalFields(this.additionalFields);
    accountEntity.setAccountContactAssociationsId(this.id);
    return accountEntity;
  }

  @JsonAnySetter
  public void setAdditionalField(String fieldName, Object value) {
    fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
        fieldName);
    if (!fieldName.equals("attributes") && !fieldName.equals("Contacts")) {
      additionalFields.put(fieldName, value);
    }
  }

  @Data
  public static class ContactsWrapper {
    private List<Contact> records = new ArrayList<>();
  }

  @Data
  @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
  public static class Contact {
    private String id;
    private String name;
    private Map<String, Object> additionalFields = new HashMap<>();

    public ContactEntity getContactEntity() {
      ContactEntity contactEntity = new ContactEntity();
      contactEntity.setId(this.id);
      contactEntity.setName(this.name);
      contactEntity.setAdditionalFields(this.additionalFields);
      return contactEntity;
    }

    @JsonAnySetter
    public void setAdditionalField(String fieldName, Object value) {
      fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
          fieldName);
      if (!fieldName.equals("attributes")) {
        additionalFields.put(fieldName, value);
      }
    }

  }

}