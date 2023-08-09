package com.salessparrow.api.dto.formatter;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNoteDetailsFormatterDto {
    private String id;
    private String creator;
    private String text;
    private Date lastModifiedTime;

    public GetNoteDetailsFormatterDto(String id, String creator, String text, Date lastModifiedTime){
        this.id = id;
        this.creator = creator;
        this.text = text;
        this.lastModifiedTime = lastModifiedTime;
    }
    
}
