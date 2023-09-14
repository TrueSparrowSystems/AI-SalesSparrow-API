package com.salessparrow.api.dto.formatter;

import lombok.Data;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.AccountContactAssociationsEntity;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.entities.ContactEntity;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAccountsFormatterDto {

	private List<String> accountIds;

	private Map<String, AccountEntity> accountMapById;

	private Map<String, ContactEntity> contactMapById;

	private Map<String, AccountContactAssociationsEntity> accountContactAssociationsMapById;

}