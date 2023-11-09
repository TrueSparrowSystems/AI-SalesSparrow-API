package com.salessparrow.api.dto.formatter;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.EventEntity;

import lombok.Data;

/**
 * GetEventsListFormatterDto is a DTO class for the GetEventsListFormatterDto response.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetEventsListFormatterDto {

	private List<String> eventIds;

	private Map<String, EventEntity> eventMapById;

}
