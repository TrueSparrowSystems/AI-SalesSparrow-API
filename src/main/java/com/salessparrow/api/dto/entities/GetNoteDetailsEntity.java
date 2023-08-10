package com.salessparrow.api.dto.entities;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * Get the content of a note
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNoteDetailsEntity {
    private String id;
    private String creator;
    private String text;
    private Date lastModifiedTime;

    public GetNoteDetailsEntity(String id, String creator, String text, Date lastModifiedTime){
        this.id = id;
        this.creator = creator;
        this.text = text;
        this.lastModifiedTime = lastModifiedTime;
    }
}
