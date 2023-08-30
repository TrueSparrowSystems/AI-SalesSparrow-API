package com.salessparrow.api.dto.entities;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountEntity {

	private String id;

	private String name;

	private Map<String, Object> additionalFields;

	private String accountContactAssociationsId;

}