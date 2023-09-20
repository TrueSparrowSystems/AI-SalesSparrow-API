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

		String todayDate = getTodaysDate();

		return "{\n" + "  \"model\": \"gpt-3.5-turbo-0613\",\n" + "  \"messages\": [\n" + "    {\n"
				+ "      \"role\": \"system\",\n"
				+ "      \"content\": \"You are an AI assistant that provides suggestions for creating tasks in CRM based solely on the content of the input message. The content of task if any found should only be from input message. If no task suggestions are found in the input message, return empty data. If task suggestions are found, they should include description and due date. Due Date format should be YYYY-MM-DD. Today's date is "
				+ todayDate
				+ ". Use the functions provided to determine task suggestions. If no tasks are possible for the given input message, return an empty response. For example, If Input Message: Had a call with product team. They need a pitch desk by tomorrow. Then it should return description: Create pitch deck. For 2nd Example, If Input Message: 3 tasks. Then it should return empty response as no tasks can be suggested from input message\"\n"
				+ "    },\n" + "    {\n" + "      \"role\": \"user\",\n" + "      \"content\": \" Input message: \\n"
				+ text + "\\n\"\n" + "    }\n" + "  ],\n" + "  \"functions\": [\n" + "    {\n"
				+ "      \"name\": \"suggest_actions\",\n"
				+ "      \"description\": \"This is function for suggesting actions in crm(example salesforce, freshsales) based on input message.\",\n"
				+ "      \"parameters\": {\n" + "        \"type\": \"object\",\n" + "        \"properties\": {\n"
				+ "          \"add_task\": {\n" + "            \"name\": \"add_task\",\n"
				+ "            \"description\": \"Tasks using input message. The task should be created only from the input message and if no task can be generated from input message then empty data should be returned.\",\n"
				+ "            \"type\": \"array\",\n" + "            \"items\": {\n"
				+ "              \"type\": \"object\",\n" + "               \"properties\": {\n"
				+ "                \"description\": {\n" + "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"Description for task to add.\"\n" + "                },\n"
				+ "                \"due_date\": {\n" + "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"Due date for task in YYYY-MM-DD format. Today's date is "
				+ todayDate + ".\"\n" + "                }\n" + "              }\n" + "            }\n"
				+ "          }\n" + "        }\n" + "      }\n" + "    }\n" + "  ]\n" + "}";
	}

	/**
	 * Todays date in yyyy-MM-dd format.
	 * @return
	 */
	public String getTodaysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

}
