package com.salessparrow.api.dto.formatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.GetNoteDetailsEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNoteDetailsFormatterDto {

    private GetNoteDetailsEntity note_details;
    
    public GetNoteDetailsFormatterDto(GetNoteDetailsEntity note_details){
        this.note_details = note_details;
    }
}
