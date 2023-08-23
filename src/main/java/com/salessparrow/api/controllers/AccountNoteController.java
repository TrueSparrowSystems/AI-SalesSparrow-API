package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.dto.requestMapper.NoteDto;
import com.salessparrow.api.services.accountNotes.CreateAccountNoteService;
import com.salessparrow.api.services.accountNotes.DeleteAccountNoteService;
import com.salessparrow.api.services.accountNotes.GetAccountNoteDetailsService;
import com.salessparrow.api.services.accountNotes.GetAccountNotesListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts/{account_id}/notes")
@Validated
public class AccountNoteController {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(AccountNoteController.class);

  @Autowired
  private GetAccountNotesListService getNotesListService;

  @Autowired
  private GetAccountNoteDetailsService getNoteDetailsService;

  @Autowired
  private CreateAccountNoteService createNoteService;

  @Autowired
  private DeleteAccountNoteService deleteAccountNoteService;

  @PostMapping("")
  public ResponseEntity<CreateNoteFormatterDto> addNoteToAccount(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId, 
    @Valid @RequestBody NoteDto note
  ) {
    logger.info("Create Note request received");

    CreateNoteFormatterDto createNoteFormatterDto = createNoteService.createNote(request, accountId, note);

    return ResponseEntity.ok().body(createNoteFormatterDto);
  }

  @GetMapping("")
  public ResponseEntity<GetNotesListFormatterDto> getNotesList(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId
  ) {
    logger.info("Get Note List request received");

    GetNotesListFormatterDto getNotesListResponse = getNotesListService.getNotesList(request, accountId);

    return ResponseEntity.ok().body(getNotesListResponse);
  }
  
  @GetMapping("/{note_id}")
  public ResponseEntity<GetNoteDetailsFormatterDto> getNoteFromAccount(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId, 
    @PathVariable("note_id") String noteId
  ) {
    logger.info("Get Note request received");

    GetNoteDetailsFormatterDto getNoteDetailsResponse = getNoteDetailsService.getNoteDetails(request, noteId);
    
    return ResponseEntity.ok().body(getNoteDetailsResponse);
  }

  @DeleteMapping("/{note_id}")
  public ResponseEntity<GetNoteDetailsFormatterDto> deleteNote(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId, 
    @PathVariable("note_id") String noteId
  ) {
    logger.info("Delete Note request received");

    deleteAccountNoteService.deleteAcountNote(request, accountId, noteId);
    
    return ResponseEntity.noContent().build();
  }
}
