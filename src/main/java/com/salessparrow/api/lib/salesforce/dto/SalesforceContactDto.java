package com.salessparrow.api.lib.salesforce.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.base.CaseFormat;
import com.salessparrow.api.dto.entities.ContactEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceContactDto {

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
		fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
		if (!fieldName.equals("attributes")) {
			additionalFields.put(fieldName, value);
		}
	}

}