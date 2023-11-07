package com.salessparrow.api.dto.formatter;

import lombok.Data;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.AccountContactAssociationsEntity;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.entities.ContactEntity;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAccountDetailsFormatterDto {

	private AccountEntity account;

	private Map<String, ContactEntity> contactMapById;

	private Map<String, AccountContactAssociationsEntity> accountContactAssociationsMapById;

}