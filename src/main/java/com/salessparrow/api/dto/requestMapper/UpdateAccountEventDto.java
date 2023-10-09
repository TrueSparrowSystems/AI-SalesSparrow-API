package com.salessparrow.api.dto.requestMapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.lib.customAnnotations.ValidDatetimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateAccountEventDto {

	@NotBlank(message = "missing_description")
	@Size(max = 32000, message = "description_too_long")
	private String description;

	@NotNull(message = "missing_start_datetime")
	@ValidDatetimeFormat(message = "invalid_start_datetime")
	private String startDatetime;

	@NotNull(message = "missing_end_datetime")
	@ValidDatetimeFormat(message = "invalid_end_datetime")
	private String endDatetime;

}
