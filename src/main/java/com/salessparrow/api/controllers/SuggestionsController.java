package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CrmActionSuggestionsFormatterDto;
import com.salessparrow.api.dto.requestMapper.CrmActionsSuggestionsDto;
import com.salessparrow.api.services.suggestions.CrmActionsSuggestionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/suggestions")
@Validated
public class SuggestionsController {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(SuggestionsController.class);

  @Autowired
  private CrmActionsSuggestionsService crmActionsSuggestionsService;

  @PostMapping("/crm-actions")
  public CrmActionSuggestionsFormatterDto getCrmActionSuggestions(
     @Valid @RequestBody CrmActionsSuggestionsDto crmActionsSuggestionsDto) {
    logger.info("Crm actions suggestions request received");

    return crmActionsSuggestionsService.getSuggestions(crmActionsSuggestionsDto);
  }

}
