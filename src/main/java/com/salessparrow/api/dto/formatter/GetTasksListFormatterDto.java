package com.salessparrow.api.dto.formatter;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.TaskEntity;

import lombok.Data;

/**
 * GetTasksListFormatterDto is a DTO class for the GetTasksListFormatterDto response.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetTasksListFormatterDto {

	private List<String> taskIds;

	private Map<String, TaskEntity> taskMapById;

}
