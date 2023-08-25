package com.salessparrow.api.services.accounts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.NextPagePayloadEntity;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.formatter.PaginationIdentifierFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsFeedDto;
import com.salessparrow.api.dto.responseMapper.GetAccountsFeedResponseDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.UserLoginCookieAuth;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.crmActions.getAccounts.GetAccountsFactory;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.AccountConstants;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountsFeedService is a service class for the GetAccountsFeed action for
 * the CRM.
 */
@Service
public class GetAccountsFeedService {
  @Autowired
  private GetAccountsFactory getAccountsFactory;

  @Autowired
  private Util util;

  Logger logger = LoggerFactory.getLogger(UserLoginCookieAuth.class);

  int offset;
  int pageNumber = 1;
  String searchTerm = null;
  ObjectMapper mapper = new ObjectMapper();

  /**
   * Get accounts feed method
   * 
   * @param request            HttpServletRequest
   * @param getAccountsFeedDto GetAccountsFeedDto
   * @return GetAccountsFeedResponseDto
   */
  public GetAccountsFeedResponseDto getAccountsFeed(HttpServletRequest request,
      GetAccountsFeedDto getAccountsFeedDto) {
    logger.info("Getting accounts feed");

    User currentUser = (User) request.getAttribute("current_user");
    offset = 0;

    if (getAccountsFeedDto.getPagination_identifier() != null) {
      logger.info("Pagination identifier found");
      String decodedPaginationIdentifier = util.base64Decode(getAccountsFeedDto.getPagination_identifier());
      PaginationIdentifierFormatterDto paginationIdentifierObj = null;
      try {
        paginationIdentifierObj = mapper.readValue(decodedPaginationIdentifier, PaginationIdentifierFormatterDto.class);
      } catch (Exception e) {
        throw new CustomException(
            new ErrorObject(
                "s_a_gafs_gaf_1",
                "something_went_wrong",
                e.getMessage()));
      }
      pageNumber = paginationIdentifierObj.getPageNumber();
      offset = (pageNumber - 1) * AccountConstants.PAGINATION_LIMIT;
    }

    return prepareResponse(getAccountsFactory.getAccounts(currentUser, searchTerm,
        AccountConstants.FEED_VIEW_KIND, offset));
  }

  /**
   * Preparing response
   * 
   * @param accountsFactoryRes GetAccountsFormatterDto
   * @return GetAccountsFeedResponseDto
   */
  private GetAccountsFeedResponseDto prepareResponse(GetAccountsFormatterDto accountsFactoryRes) {
    logger.info("Preparing response");
    GetAccountsFeedResponseDto accountsFeedResponse = new GetAccountsFeedResponseDto();

    accountsFeedResponse.setAccountIds(accountsFactoryRes.getAccountIds());
    accountsFeedResponse.setAccountMapById(accountsFactoryRes.getAccountMapById());
    accountsFeedResponse.setContactMapById(accountsFactoryRes.getContactMapById());
    accountsFeedResponse.setAccountContactAssociationsMapById(
        accountsFactoryRes.getAccountContactAssociationsMapById());

    NextPagePayloadEntity nextPagePayload = new NextPagePayloadEntity();

    logger.info("Preparing pagination identifier");
    PaginationIdentifierFormatterDto paginationIdentifier = new PaginationIdentifierFormatterDto();
    paginationIdentifier.setPageNumber(pageNumber + 1);

    try {
      String paginationJson = mapper.writeValueAsString(paginationIdentifier);
      nextPagePayload.setPaginationIdentifier(util.base64Encode(paginationJson));
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "s_a_gafs_gaf_pr_1",
              "something_went_wrong",
              e.getMessage()));

    }

    accountsFeedResponse.setNextPagePayload(nextPagePayload);
    return accountsFeedResponse;
  }

}
