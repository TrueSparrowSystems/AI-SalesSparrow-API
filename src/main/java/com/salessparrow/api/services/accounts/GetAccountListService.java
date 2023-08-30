package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.dto.responseMapper.GetAccountListResponseDto;
import com.salessparrow.api.lib.crmActions.getAccounts.GetAccountsFactory;
import com.salessparrow.api.lib.globalConstants.AccountConstants;

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
	 * @return GetAccountsFormatterDto
	 **/
	public GetAccountListResponseDto getAccounts(HttpServletRequest request, GetAccountsDto getAccountsDto) {
		User currentUser = (User) request.getAttribute("current_user");

		String formattedSearchString = "";
		if (getAccountsDto.getQ() != null) {
			formattedSearchString = formatSearchString(getAccountsDto.getQ());
		}
		GetAccountsFormatterDto getAccountsFormatterDto = getAccountsFactory.getAccounts(currentUser,
				formattedSearchString, AccountConstants.BASIC_VIEW_KIND, 0);

		GetAccountListResponseDto getAccountListResponseDto = new GetAccountListResponseDto();
		if (getAccountsFormatterDto.getAccountIds() != null) {
			getAccountListResponseDto.setAccountIds(getAccountsFormatterDto.getAccountIds());
		}
		if (getAccountsFormatterDto.getAccountMapById() != null) {
			getAccountListResponseDto.setAccountMapById(getAccountsFormatterDto.getAccountMapById());
		}
		return getAccountListResponseDto;
	}

	private String formatSearchString(String q) {
		q = q.trim();
		return q;
	}

}