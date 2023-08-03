package com.salessparrow.api.lib;

import java.util.Base64;

public class Base64Helper {
  public static String base64Encode(String originalString) {
    byte[] originalBytes = originalString.getBytes();
    byte[] encodedBytes = Base64.getEncoder().encode(originalBytes);
    return new String(encodedBytes);
  }

  public static String base64Decode(String encodedString) {
    byte[] encodedBytes = encodedString.getBytes();
    byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
    return new String(decodedBytes);
  }
}
