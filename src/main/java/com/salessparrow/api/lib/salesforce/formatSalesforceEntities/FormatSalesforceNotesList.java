package com.salessparrow.api.lib.salesforce.formatSalesforceEntities;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.dto.entities.NoteListEntity;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;

/**
 * FormatSalesforceNotesList is a class for the FormatSalesforceNotesList action for formatting the response body.
 */
public class FormatSalesforceNotesList {

    /**
     * Format the response body to get the list of content document ids
     * 
     * @param responseBody
     * @return List<String>
     */
    public List<String> formatContentDocumentId(String responseBody){

        List<String> contentDocumentIds = new ArrayList<String>();

        try {
            Util util = new Util();
            JsonNode rootNode = util.getJsonNode(responseBody);
            JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

            for (JsonNode recordNode : recordsNode) {
                String contentDocumentId = recordNode.get("ContentDocumentId").asText();
                contentDocumentIds.add(contentDocumentId);
            }
        } catch (Exception e) {
            throw new CustomException(
                new ErrorObject(
                    "l_s_fse_fsnl_1",
                    "something_went_wrong",
                    e.getMessage()
                )
            );
        }

        return contentDocumentIds;
    }

    /**
     * Format the response body to get the list of notes
     * 
     * @param responseBody
     * @return GetNotesListFormatterDto
     */
    public GetNotesListFormatterDto formatNotesList(String responseBody){

        List<String> noteIds = new ArrayList<String>();
        Map<String,NoteListEntity> noteListEntities = new HashMap<>();

        try {
            Util util = new Util();
            JsonNode rootNode = util.getJsonNode(responseBody);
            JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

            for (JsonNode recordNode : recordsNode) {
                String noteId = recordNode.get("Id").asText();
                String textPreview = recordNode.get("TextPreview").asText();
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date lastModifiedDate = new Date();
                try {
                    lastModifiedDate = dateFormat.parse(recordNode.get("LastModifiedDate").asText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String createdBy = recordNode.get("CreatedBy").get("Name").asText();


                noteIds.add(noteId);
                NoteListEntity noteListEntity = new NoteListEntity(
                    noteId,
                    createdBy,
                    textPreview,
                    lastModifiedDate
                );
                
                noteListEntities.put(noteId, noteListEntity);
            }
        } catch (Exception e) {
            throw new CustomException(
                new ErrorObject(
                    "l_s_fse_fsnl_2",
                    "something_went_wrong",
                    e.getMessage()
                )
            );
        }

        GetNotesListFormatterDto getNotesListFormatterDto = new GetNotesListFormatterDto();
        getNotesListFormatterDto.setNoteIds(noteIds);
        getNotesListFormatterDto.setNoteMapById(noteListEntities);

        return getNotesListFormatterDto;

    }
}
