package com.salessparrow.api.services.suggestions;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.controllers.SuggestionsController;
import com.salessparrow.api.dto.formatter.CrmActionSuggestionsFormatterDto;
import com.salessparrow.api.dto.requestMapper.CrmActionsSuggestionsDto;
import com.salessparrow.api.lib.GetCrmActionSuggestions;

/**
 * CrmActionsSuggestionsService is a service class for the crm actions suggestions.
 */
@Service
public class CrmActionsSuggestionsService {
  @Autowired
  private GetCrmActionSuggestions getCrmActionSuggestions;

  private Logger logger = org.slf4j.LoggerFactory.getLogger(SuggestionsController.class);
  
  public CrmActionSuggestionsFormatterDto getSuggestions(CrmActionsSuggestionsDto crmActionsSuggestionsDto) {
    logger.info("Crm actions service called");
    String text = crmActionsSuggestionsDto.getText();

    return getCrmActionSuggestions.getTaskSuggestions(text);
  }
}
