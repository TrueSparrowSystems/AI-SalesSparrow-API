package com.salessparrow.api.lib.salesforce.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.CrmOrganizationUserEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceCrmOrganizationUserDto {
    private String id;
    private String name;

    /**
     *  convert SalesforceCrmOrganizationUserDto into CrmOrganizationUserEntity
     */
    public CrmOrganizationUserEntity getCrmOrganizationUserEntity() {
        CrmOrganizationUserEntity crmOrganizationUserEntity = new CrmOrganizationUserEntity();
        crmOrganizationUserEntity.setId(this.id);
        crmOrganizationUserEntity.setName(this.name);

        return crmOrganizationUserEntity;
    }
}
