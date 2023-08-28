package com.salessparrow.api.dto.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * Add Task Suggestion Entity is a DTO class for the Add Task Suggestion Entity.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddTaskSuggestionEntityDto {
  private String description;
  private String dueDate;
}
