package com.salessparrow.api.lib.crmActions.deleteAccountNote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * DeleteAccountSalesforceNote is a class for the DeleteAccountNote service for the Salesforce CRM.
 **/
@Component
public class DeleteSalesforceAccountNote implements DeleteAccountNoteInterface {
  private Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteSalesforceAccountNote.class);

  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  /**
   * Deletes a note from salesforce
   * 
   * @param user
   * @param noteId
   * 
   * @return void
   **/
  public void deleteAccountNote(User user, String noteId) {
    logger.info("Delete Salesforce Account Note called");

    String salesforceUserId = user.getExternalUserId();

    String url = salesforceConstants.salesforceDeleteNoteUrl(noteId);

    CompositeRequestDto compositeReq = new CompositeRequestDto("DELETE", url, "DeleteNote");

    List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
    compositeRequests.add(compositeReq);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

    parseResponse(response.getResponseBody());
  }

  /**
   * Parse Response
   * 
   * @param responseBody
   * 
   * @return void
  **/
  public void parseResponse(String responseBody) {

    Util util = new Util();
    JsonNode rootNode = util.getJsonNode(responseBody);

    JsonNode deleteNoteCompositeResponse = rootNode.get("compositeResponse").get(0);
    Integer deleteNoteStatusCode = deleteNoteCompositeResponse.get("httpStatusCode").asInt();
    
    if (deleteNoteStatusCode != 200 && deleteNoteStatusCode != 201 && deleteNoteStatusCode != 204) {
      String errorBody = deleteNoteCompositeResponse.get("body").asText();
      String errorCode = deleteNoteCompositeResponse.get("body").get(0).get("errorCode").asText();

      // Error handling
      if (errorCode.equals("ENTITY_IS_DELETED") || errorCode.equals("NOT_FOUND") || errorCode.equals("MALFORMED_ID")) {
        throw new CustomException(
          new ParamErrorObject(
            "l_ca_dan_dasn_pr_1", 
            errorBody, 
            Arrays.asList("invalid_note_id")));
      } else if (errorCode.equals("INSUFFICIENT_ACCESS_OR_READONLY")) {
        throw new CustomException(
          new ErrorObject(
            "l_ca_dan_dasn_pr_2",
            "forbidden_api_request",
            errorBody));
      } else {
        throw new CustomException(
          new ErrorObject(
            "l_ca_dan_dasn_pr_3",
            "something_went_wrong",
            errorBody));
      }
    }
  }
}