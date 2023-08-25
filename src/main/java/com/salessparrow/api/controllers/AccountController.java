package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsFeedDto;
import com.salessparrow.api.dto.requestMapper.NoteDto;
import com.salessparrow.api.dto.responseMapper.GetAccountListResponseDto;
import com.salessparrow.api.dto.responseMapper.GetAccountsFeedResponseDto;
import com.salessparrow.api.services.accounts.CreateNoteService;
import com.salessparrow.api.services.accounts.GetAccountListService;
import com.salessparrow.api.services.accounts.GetAccountsFeedService;
import com.salessparrow.api.services.accounts.GetNoteDetailsService;
import com.salessparrow.api.services.accounts.GetNotesListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private GetAccountListService getAccountListService;

  @Autowired
  private GetAccountsFeedService getAccountsFeedService;

  @Autowired
  private GetNotesListService getNotesListService;

  @Autowired
  private GetNoteDetailsService getNoteDetailsService;

  @Autowired
  private CreateNoteService createNoteService;

  @GetMapping("")
  public ResponseEntity<GetAccountListResponseDto> getAccounts(
      HttpServletRequest request,
      @Valid @ModelAttribute GetAccountsDto getAccountsDto) {
    logger.info("Request received");

    GetAccountListResponseDto getAccountsResponse = getAccountListService.getAccounts(request, getAccountsDto);

    return ResponseEntity.ok().body(getAccountsResponse);
  }

  @GetMapping("/feed")
  public ResponseEntity<GetAccountsFeedResponseDto> getFeed(
      HttpServletRequest request,
      @Valid @ModelAttribute GetAccountsFeedDto getAccountsFeedDto) {
    logger.info("Request received");

    GetAccountsFeedResponseDto getAccountsFeedResponse = getAccountsFeedService.getAccountsFeed(request,
        getAccountsFeedDto);

    return ResponseEntity.ok().body(getAccountsFeedResponse);
  }

  @PostMapping("/{account_id}/notes")
  public ResponseEntity<CreateNoteFormatterDto> addNoteToAccount(
      HttpServletRequest request,
      @PathVariable("account_id") String accountId,
      @Valid @RequestBody NoteDto note) {
    CreateNoteFormatterDto createNoteFormatterDto = createNoteService.createNote(request, accountId, note);

    return ResponseEntity.ok().body(createNoteFormatterDto);
  }

  @GetMapping("/{account_id}/notes")
  public ResponseEntity<GetNotesListFormatterDto> getNotesList(HttpServletRequest request,
      @PathVariable("account_id") String accountId) {

    GetNotesListFormatterDto getNotesListResponse = getNotesListService.getNotesList(request, accountId);

    return ResponseEntity.ok().body(getNotesListResponse);
  }

  @GetMapping("/{account_id}/notes/{note_id}")
  public ResponseEntity<GetNoteDetailsFormatterDto> getNoteFromAccount(
      HttpServletRequest request,
      @PathVariable("account_id") String accountId,
      @PathVariable("note_id") String noteId) {

    GetNoteDetailsFormatterDto getNoteDetailsResponse = getNoteDetailsService.getNoteDetails(request, noteId);

    return ResponseEntity.ok().body(getNoteDetailsResponse);
  }
}
