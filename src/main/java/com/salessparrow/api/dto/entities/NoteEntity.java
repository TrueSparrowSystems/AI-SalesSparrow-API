package com.salessparrow.api.dto.entities;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * NoteListEntity is a DTO class for the NoteListEntity.
 */
@Data
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

}
