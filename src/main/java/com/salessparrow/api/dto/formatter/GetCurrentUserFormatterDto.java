package com.salessparrow.api.dto.formatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.CurrentUserEntityDto;

import lombok.Data;

/**
 * Get current user formatter DTO.
 *
 * @param current_user
 * @return GetCurrentUserFormatterDto
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCurrentUserFormatterDto {

	private CurrentUserEntityDto currentUser;

}