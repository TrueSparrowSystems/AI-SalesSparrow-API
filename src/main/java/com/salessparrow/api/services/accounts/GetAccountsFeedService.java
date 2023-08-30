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

  Logger logger = LoggerFactory.getLogger(GetAccountsFeedService.class);
  int pageNumber;

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
    pageNumber = 1;
    String searchTerm = null;

    int offset = calculateOffset(getAccountsFeedDto.getPagination_identifier());

    return prepareResponse(getAccountsFactory.getAccounts(currentUser, searchTerm,
        AccountConstants.FEED_VIEW_KIND, offset));
  }

  /**
   * Calculate offset for pagination using pagination identifier
   * 
   * @param paginationIdentifier String
   * @return int
   */
  private int calculateOffset(String paginationIdentifier) {
    int offset = 0;

    if (paginationIdentifier != null) {
      logger.info("Pagination identifier found");
      String decodedPaginationIdentifier = Util.base64Decode(paginationIdentifier);
      PaginationIdentifierFormatterDto paginationIdentifierObj = null;
      try {
        ObjectMapper mapper = new ObjectMapper();
        paginationIdentifierObj = mapper.readValue(decodedPaginationIdentifier, PaginationIdentifierFormatterDto.class);
      } catch (Exception e) {
        throw new CustomException(
            new ErrorObject(
                "s_a_gafs_gaf_1",
                "pagination_identifier_parsing_error",
                e.getMessage()));
      }
      pageNumber = paginationIdentifierObj.getPageNumber();
      offset = (pageNumber - 1) * AccountConstants.PAGINATION_LIMIT;
    }

    return offset;
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

    setResponseFields(accountsFeedResponse, accountsFactoryRes);
    setNextPagePayload(accountsFeedResponse, pageNumber);

    return accountsFeedResponse;
  }

  private void setResponseFields(GetAccountsFeedResponseDto response, GetAccountsFormatterDto factoryRes) {
    response.setAccountIds(factoryRes.getAccountIds());
    response.setAccountMapById(factoryRes.getAccountMapById());
    response.setContactMapById(factoryRes.getContactMapById());
    response.setAccountContactAssociationsMapById(factoryRes.getAccountContactAssociationsMapById());
  }

  private void setNextPagePayload(GetAccountsFeedResponseDto response, int pageNumber) {
    logger.info("Preparing pagination identifier");
    PaginationIdentifierFormatterDto paginationIdentifier = new PaginationIdentifierFormatterDto();
    paginationIdentifier.setPageNumber(pageNumber + 1);

    try {
      ObjectMapper mapper = new ObjectMapper();
      String paginationJson = mapper.writeValueAsString(paginationIdentifier);
      NextPagePayloadEntity nextPagePayload = new NextPagePayloadEntity();
      nextPagePayload.setPaginationIdentifier(Util.base64Encode(paginationJson));
      response.setNextPagePayload(nextPagePayload);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "s_a_gafs_gaf_pr_1",
              "something_went_wrong",
              e.getMessage()));
    }
  }

}
