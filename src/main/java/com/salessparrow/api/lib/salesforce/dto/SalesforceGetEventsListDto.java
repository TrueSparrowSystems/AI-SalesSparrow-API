package com.salessparrow.api.lib.salesforce.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.EventEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceGetEventsListDto {

	private String id;

	private String description;

	private CreatedBy createdBy;

	private String startDateTime;

	private String endDateTime;

	private Date lastModifiedDate;

	@Data
	@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
	private class CreatedBy {

		private String name;

	}

	@Data
	@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
	private class Owner {

		private String name;

	}

	public EventEntity eventEntity() {

		EventEntity eventEntity = new EventEntity();
		eventEntity.setId(this.id);
		eventEntity.setCreatorName(this.createdBy.name);
		eventEntity.setDescription(this.description);
		eventEntity.setStartDatetime(this.startDateTime);
		eventEntity.setEndDatetime(this.endDateTime);
		eventEntity.setLastModifiedTime(this.lastModifiedDate);

		return eventEntity;
	}

}
