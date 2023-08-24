package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;

@Component
public class OpenAiConstants {

  public String chatCompletionUrl() {
    return "https://api.openai.com/v1/chat/completions";
  }
}
