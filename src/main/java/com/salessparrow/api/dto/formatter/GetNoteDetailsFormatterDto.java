package com.salessparrow.api.dto.formatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.NoteDetailEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNoteDetailsFormatterDto {

    private NoteDetailEntity note_detail;
    
    public GetNoteDetailsFormatterDto(NoteDetailEntity noteDetail){
        this.note_detail = noteDetail;
    }
}
