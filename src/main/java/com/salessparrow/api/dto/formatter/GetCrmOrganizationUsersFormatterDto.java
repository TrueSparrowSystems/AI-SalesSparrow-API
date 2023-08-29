package com.salessparrow.api.dto.formatter;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.CrmOrganizationUserEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCrmOrganizationUsersFormatterDto {
    private List<String> crmOrganizationUserIds;
    private Map<String, CrmOrganizationUserEntity> crmOrganizationUserMapById;
}
