package com.salessparrow.api.lib.salesforce.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.NoteEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceGetNotesListDto {
    private String id;
    private String textPreview;
    private CreatedBy createdBy;
    private Date lastModifiedDate;

    @Data
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    private class CreatedBy{
        private String name;
    }

    public NoteEntity noteEntity(){
        NoteEntity noteEntity = new NoteEntity(
            this.id,
            this.createdBy.name,
            this.textPreview,
            this.lastModifiedDate
        );
        return noteEntity;
    }
}
