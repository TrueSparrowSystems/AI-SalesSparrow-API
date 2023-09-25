package com.salessparrow.api.dto.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * Add Event Suggestion Entity is a DTO class for the Add Event Suggestion Entity.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddEventSuggestionEntityDto {

	private String description;

	private String startDatetime;

	private String endDatetime;

}
