package com.salessparrow.api.unit.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;

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
  public void testEncode() {
    String plainText = "Hello, World!";
    String encodedText = util.base64Encode(plainText);
    assertEquals("SGVsbG8sIFdvcmxkIQ==", encodedText);
  }

  @Test
  public void testEncodeEmptyString() {
    String plainText = "";
    String encodedText = util.base64Encode(plainText);
    assertEquals("", encodedText);
  }

  @Test
  public void testEncodeNullString() {
    String plainText = null;
    assertThrows(CustomException.class, () -> util.base64Encode(plainText));
  }

  @Test
  public void testDecode() {
    String encodedText = "SGVsbG8sIFdvcmxkIQ==";
    String decodedText = util.base64Decode(encodedText);
    assertEquals("Hello, World!", decodedText);
  }

  @Test
  public void testDecodeEmptyString() {
    String encodedText = "";
    String decodedText = util.base64Decode(encodedText);
    assertEquals("", decodedText);
  }

  @Test
  public void testDecodeNullString() {
    String encodedText = null;
    assertThrows(CustomException.class, () -> util.base64Decode(encodedText));
  }

  @Test
  public void testDecodeInvalidString() {
    String encodedText = "SGVsbG8sIFdvcmxkIQ=";
    assertThrows(CustomException.class, () -> util.base64Decode(encodedText));
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

    assertThrows(CustomException.class, () -> util.getJsonNode(invalidJsonString));
  }
}
