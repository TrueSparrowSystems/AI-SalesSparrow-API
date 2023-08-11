package com.salessparrow.api.lib;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;

/**
 * Class for utility functions.
 * 
 */
@Component
public class Util {

  /**
   * Get JsonNode from json string
   * 
   * @param jsonString
   * 
   * @return JsonNode
   */
  public JsonNode getJsonNode(String jsonString) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = null;
    try {
      jsonNode = mapper.readTree(jsonString);
    } catch (Exception e) {
      throw new CustomException(
          new ErrorObject(
              "l_u_gjn_1",
              "something_went_wrong",
              e.getMessage()));
    }
    return jsonNode;
  }
}
