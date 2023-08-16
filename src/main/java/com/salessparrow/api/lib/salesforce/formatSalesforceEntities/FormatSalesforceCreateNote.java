package com.salessparrow.api.lib.salesforce.formatSalesforceEntities;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;

public class FormatSalesforceCreateNote {
  
  public CreateNoteFormatterDto formatCreateNote(String createNoteResponse) {        
    Util util = new Util();
    JsonNode rootNode = util.getJsonNode(createNoteResponse);

    JsonNode createNoteNode = rootNode.get("compositeResponse").get(0).get("body");
    String createNoteSuccess = createNoteNode.get("success").asText();

    if (createNoteSuccess.equals("false")) {
      String errors = createNoteNode.get("errors").asText();
      throw new CustomException(
      new ErrorObject(
          "l_s_fse_fscn_fcn_1",
          "internal_server_error",
          errors)); 
    }

    JsonNode attachNoteNode = rootNode.get("compositeResponse").get(1).get("body");
    String attachNoteSuccess = attachNoteNode.get("success").asText();

    if (attachNoteSuccess.equals("false")) {
      String errors = attachNoteNode.get("errors").asText();
      throw new CustomException(
      new ErrorObject(
          "l_s_fse_fscn_fcn_2",
          "internal_server_error",
          errors)); 
    }

    String noteId = createNoteNode.get("id").asText();
    CreateNoteFormatterDto createNoteFormatterDto = new CreateNoteFormatterDto();
    createNoteFormatterDto.setNoteId(noteId);

    return createNoteFormatterDto;
  }
}
