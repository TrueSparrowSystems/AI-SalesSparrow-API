package com.salessparrow.api.dto.formatter;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.NoteEntity;

import lombok.Data;

/**
 * GetNotesListFormatterDto is a DTO class for the GetNotesListFormatterDto response.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNotesListFormatterDto {
    private List<String> noteIds;
    private Map<String, NoteEntity> noteMapById;
}
