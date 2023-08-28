package com.salessparrow.api.lib.crmActions.getAccountTasksList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.TaskEntity;
import com.salessparrow.api.dto.formatter.GetTasksListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetTasksListDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

/**
 * GetSalesforceAccountTasksList is a class for the GetAccountTasksList service for the Salesforce CRM.
 */
@Component
public class GetSalesforceAccountTasksList {
  Logger logger = LoggerFactory.getLogger(GetSalesforceAccountTasksList.class);

  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  /**
   * Get the list of tasks for a given account in salesforce
   * 
   * @param user
   * @param accountId
   * 
   * @return GetTasksListFormatterDto
   **/
  public GetTasksListFormatterDto getAccountTasksList(User user, String accountId) {
    logger.info("Salesforce getAccountTasksList action called");

    String salesforceUserId = user.getExternalUserId();

    SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
    String query = salesforceQuery.getAccountTasksQuery(accountId);

    String url = salesforceConstants.queryUrlPath() + query;

    CompositeRequestDto compositeReq = new CompositeRequestDto("GET", url, "GetTasksList");

    List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
    compositeRequests.add(compositeReq);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

    return parseResponse(response.getResponseBody());
  }

  /**
   * Parse Response
   * 
   * @param responseBody
   * 
   * @return GetTasksListFormatterDto
  **/
  public GetTasksListFormatterDto parseResponse(String responseBody) {
    
    List<String> taskIds = new ArrayList<String>();
    Map<String, TaskEntity> taskIdToEntityMap = new HashMap<>();

    Util util = new Util();
    JsonNode rootNode = util.getJsonNode(responseBody);

    JsonNode getTasksCompositeResponse = rootNode.get("compositeResponse").get(0);
    Integer getTasksStatusCode = getTasksCompositeResponse.get("httpStatusCode").asInt();
    
    if (getTasksStatusCode != 200 && getTasksStatusCode != 201) {
      String errorBody = getTasksCompositeResponse.get("body").asText();

      if (getTasksStatusCode == 400) {
        throw new CustomException(
          new ParamErrorObject(
            "l_ca_gatl_gsatl_pr_1", 
            errorBody, 
            Arrays.asList("invalid_account_id")));
      } else {
        throw new CustomException(
          new ErrorObject(
            "l_ca_gatl_gsatl_pr_2",
            "something_went_wrong",
            errorBody));
      }
    }

    JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");;

    for (JsonNode recordNode : recordsNode) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      SalesforceGetTasksListDto salesforceGetTasksListDto = mapper.convertValue(recordNode, SalesforceGetTasksListDto.class);
      TaskEntity taskEntity = salesforceGetTasksListDto.taskEntity();

      taskIds.add(taskEntity.getId());
      taskIdToEntityMap.put(taskEntity.getId(), taskEntity);
    }

    GetTasksListFormatterDto getTasksListFormatterDto = new GetTasksListFormatterDto();
    getTasksListFormatterDto.setTaskMapById(taskIdToEntityMap);
    getTasksListFormatterDto.setTaskIds(taskIds);

    return getTasksListFormatterDto;
  }
}
