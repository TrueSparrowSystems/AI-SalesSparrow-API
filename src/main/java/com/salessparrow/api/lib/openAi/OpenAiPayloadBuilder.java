package com.salessparrow.api.lib.openAi;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * OpenAiPayloadBuilder is a class for the payload builder for the open ai.
 */
@Component
public class OpenAiPayloadBuilder {

  /**
   * Payload for crm actions suggestions.
   * @param text
   * @return
   */
  public String payloadForCrmActionsSuggestions(String text) {
    
    return "{\n" +
    "  \"model\": \"gpt-3.5-turbo-0613\",\n" +
    "  \"messages\": [\n" +
    "    {\n" +
    "      \"role\": \"user\",\n" +
    "      \"content\": \"You are an AI assistant which gives suggestion on creating task using the input message.Only use the functions you have been provided with. \\nInput message: \\n" + text + "\\n\"\n" +
    "    }\n" +
    "  ],\n" +
    "  \"functions\": [\n" +
    "    {\n" +
    "      \"name\": \"suggest_actions\",\n" +
    "      \"description\": \"This is function for suggesting actions in crm(example salesforce, freshsales) based on input message.\",\n" +
    "      \"parameters\": {\n" +
    "        \"type\": \"object\",\n" +
    "        \"properties\": {\n" +
    "          \"add_task\": {\n" +
    "            \"name\": \"add_task\",\n" +
    "            \"description\": \"Tasks using input message.\",\n" +
    "            \"type\": \"array\",\n" +
    "            \"items\": {\n" +
    "              \"type\": \"object\",\n" +
    "               \"properties\": {\n" +
    "                \"description\": {\n" +
    "                  \"type\": \"string\",\n" +
    "                  \"description\": \"Description for task to add. This is mandatory\"\n" +
    "                },\n" +
    "                \"due_date\": {\n" +
    "                  \"type\": \"string\",\n" +
    "                  \"description\": \"Due date for task. Must be in YYYY-MM-DD format. This is mandatory.\" \n" +
    "                }\n" +
    "              },\n" +
    "              \"required\": [\"description\", \"due_date\"]\n" + 
    "            }\n" +
    "          }\n" +
    "        },\n" +
    "        \"required\": [\"add_task\"]\n" +
    "      }\n" +
    "    }\n" +
    "  ]\n" +
    "}";
  }

  // get todays date in YYYY-MM-DD format
  public String getTodaysDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(new Date());
  }
}
