package com.salessparrow.api.services.accounts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.crmActions.getAccounts.GetAccountsFactory;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountListService is a service class for the GetAccounts action for the CRM.
 */
@Service
public class GetAccountListService {
  @Autowired
  private GetAccountsFactory getAccountsFactory;
  
  /**
     * Get the list of accounts for a given search term
     * @param request
     * @param getAccountsDto
     * 
     * @return GetAccountsFormatterDto
     **/
  public GetAccountsFormatterDto getAccounts(HttpServletRequest request, GetAccountsDto getAccountsDto) {
    User currentUser = (User) request.getAttribute("current_user");

    String formattedSearchString = "";
    if(getAccountsDto.getQ() != null){
      formattedSearchString = formatSearchString(getAccountsDto.getQ());
    }
    return getAccountsFactory.getAccounts(currentUser, formattedSearchString);
  }

  private String formatSearchString(String q) {
    q = q.trim();
    try {
      return URLEncoder.encode(q, "UTF-8");
    } catch (UnsupportedEncodingException e) {

      List<String> paramError = new ArrayList<>();
      paramError.add("invalid_search_term");

      throw new CustomException(
        new ParamErrorObject(
          "s_a_gals_fss",
          e.getMessage(),
          paramError
        )
      );
    }
  }
}
