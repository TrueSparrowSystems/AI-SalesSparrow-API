package com.salessparrow.api.dto.formatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.NoteDetailEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNoteDetailsFormatterDto {

	private NoteDetailEntity noteDetail;

	public GetNoteDetailsFormatterDto(NoteDetailEntity noteDetail) {
		this.noteDetail = noteDetail;
	}

}
