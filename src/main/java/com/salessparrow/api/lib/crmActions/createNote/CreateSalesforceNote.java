package com.salessparrow.api.lib.crmActions.createNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.NoteDto;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.lib.Base64Helper;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceCreateNote;

/**
 * CreateSalesforceNote is a class that creates a note in Salesforce and attaches it to an account.
 */
@Component
public class CreateSalesforceNote implements CreateNoteInterface {

  Logger logger = LoggerFactory.getLogger(CreateSalesforceNote.class);
  
  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  @Autowired
  private Base64Helper base64Helper;

  /**
   * Create a note for a given account.
   * 
   * @param user
   * @param accountId
   * @param note
   * @return CreateNoteFormatterDto
   */
  public CreateNoteFormatterDto createNote(SalesforceUser user, String accountId, NoteDto note) {
    CreateNoteFormatterDto createNoteFormatterDto = createSalesforceNote(user, accountId, note);

    return formatResponse(createNoteFormatterDto);
  }

  /**
   * Create a note in Salesforce and attach it to an account.
   *
   * @param user
   * @param accountId
   * @param note
   * @return CreateNoteFormatterDto
   */
  private CreateNoteFormatterDto createSalesforceNote(SalesforceUser user, String accountId, NoteDto note) {
    String salesforceUserId = user.getExternalUserId();

    String noteTitle = getNoteTitleFromContent(note);
    String encodedNoteContent = base64Helper.base64Encode(note.getText());

    Map<String, String> createNoteBody = new HashMap<String, String>();
    createNoteBody.put("Title", noteTitle);
    createNoteBody.put("Content", encodedNoteContent);

    CompositeRequestDto createNoteCompositeRequestDto = new CompositeRequestDto(
      "POST", 
      salesforceConstants.salesforceCreateNoteUrl(), 
      "CreateNote",
      createNoteBody
    );

    Map<String, String> attachNoteBody = new HashMap<String, String>();
    attachNoteBody.put("ContentDocumentId", "@{CreateNote.id}");
    attachNoteBody.put("LinkedEntityId", accountId);

    CompositeRequestDto attachNoteCompositeRequestDto = new CompositeRequestDto(
      "POST", 
      salesforceConstants.salesforceAttachNoteUrl(), 
      "AttachNote",
      attachNoteBody
    );

    List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
    compositeRequests.add(createNoteCompositeRequestDto);
    compositeRequests.add(attachNoteCompositeRequestDto);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

    FormatSalesforceCreateNote formatSalesforceCreateNote = new FormatSalesforceCreateNote();
    CreateNoteFormatterDto createNoteFormatterDto = formatSalesforceCreateNote.formatCreateNote(response.getResponseBody());

    return createNoteFormatterDto;
  }

  /**
   * Format the response from Salesforce.
   * @param createNoteFormatterDto
   * @return CreateNoteFormatterDto - formatted response
   */
  private CreateNoteFormatterDto formatResponse(CreateNoteFormatterDto createNoteFormatterDto) {
    return createNoteFormatterDto;
  }

  /**
   * Get the first 50 characters of the note content.
   * 
   * @param note - note dto
   * @return String
   */
  private String getNoteTitleFromContent(NoteDto note) {
    if (note.getText().length() < 50) {
      return note.getText();
    }

    return note.getText().substring(0, 50);
  }
}
