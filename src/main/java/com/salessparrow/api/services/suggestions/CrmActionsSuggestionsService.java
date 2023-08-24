package com.salessparrow.api.services.suggestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  
  public CrmActionSuggestionsFormatterDto getSuggestions(CrmActionsSuggestionsDto crmActionsSuggestionsDto) {
    String text = crmActionsSuggestionsDto.getText();

    return getCrmActionSuggestions.getTaskSuggestions(text);
  }
}
