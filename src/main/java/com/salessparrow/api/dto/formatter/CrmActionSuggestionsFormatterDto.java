package com.salessparrow.api.dto.formatter;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.AddTaskSuggestionEntityDto;

import lombok.Data;

/**
 * CrmActionSuggestionsFormatterDto is a class for the formatter of the crm action
 * suggestions.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CrmActionSuggestionsFormatterDto {

	private List<AddTaskSuggestionEntityDto> addTaskSuggestions;

}
