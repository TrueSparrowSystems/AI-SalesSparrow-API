package com.salessparrow.api.lib;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
      e.printStackTrace();
    }
    return jsonNode;
  }
}
