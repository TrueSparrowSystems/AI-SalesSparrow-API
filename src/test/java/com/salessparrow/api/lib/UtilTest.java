package com.salessparrow.api.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class UtilTest {

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private Util util;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetJsonNode_ValidJson() throws Exception {
    String jsonString = "{\"key\": \"value\"}";

    JsonNode resultJsonNode = util.getJsonNode(jsonString);

    assertEquals("value", resultJsonNode.get("key").asText());
  }

  @Test
  public void testGetJsonNode_InvalidJson() throws Exception {
    String invalidJsonString = "invalid json";

    JsonNode resultJsonNode = util.getJsonNode(invalidJsonString);

    assertEquals(null, resultJsonNode);
  }
}
