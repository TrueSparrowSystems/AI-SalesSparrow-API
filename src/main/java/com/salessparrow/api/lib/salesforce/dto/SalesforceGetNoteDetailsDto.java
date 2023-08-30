package com.salessparrow.api.lib.salesforce.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.NoteDetailEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceGetNoteDetailsDto {

	private String id;

	private CreatedBy createdBy;

	private Date lastModifiedDate;

	@Data
	@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
	private class CreatedBy {

		private String name;

	}

	public NoteDetailEntity noteDetailEntity(String noteContentResponse) {
		NoteDetailEntity noteDetailEntity = new NoteDetailEntity(this.id, this.createdBy.name, noteContentResponse,
				this.lastModifiedDate);
		return noteDetailEntity;
	}

}
