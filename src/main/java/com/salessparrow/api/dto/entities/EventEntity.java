package com.salessparrow.api.dto.entities;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * EventEntity is a DTO class for the Event List.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventEntity {

	private String id;

	private String creatorName;

	private String description;

	private String startDatetime;

	private String endDatetime;

	private Date lastModifiedTime;

}
