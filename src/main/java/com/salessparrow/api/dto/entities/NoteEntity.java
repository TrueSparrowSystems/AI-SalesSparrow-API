package com.salessparrow.api.dto.entities;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * NoteListEntity is a DTO class for the NoteListEntity.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NoteEntity {
    private String id;
    private String creator;
    private String textPreview;
    private Date lastModifiedTime;

    public NoteEntity() {
    }

    public NoteEntity(String id, String creator, String text_preview, Date last_modified_time) {
        this.id = id;
        this.creator = creator;
        this.textPreview = text_preview;
        this.lastModifiedTime = last_modified_time;
    }

    public String getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public String getTextPreview() {
        return textPreview;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setTextPreview(String text_preview) {
        this.textPreview = text_preview;
    }

    public void setLastModifiedTime(Date last_modified_time) {
        this.lastModifiedTime = last_modified_time;
    }

}
