package com.salessparrow.api.dto.requestMapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.lib.customAnnotations.ValidDateFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateTaskInAccountDto {

    @NotBlank(message = "missing_crm_organization_user_id")
    private String crmOrganizationUserId;

    @NotBlank(message = "missing_description")
    @Size(max = 32000, message = "description_too_long")
    private String description;

    @ValidDateFormat(message = "invalid_due_date")
    private String dueDate;
}
