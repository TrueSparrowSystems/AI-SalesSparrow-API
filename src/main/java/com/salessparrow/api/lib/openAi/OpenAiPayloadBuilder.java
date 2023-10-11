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

		String payload = "{\n" + "  \"model\": \"gpt-3.5-turbo-0613\",\n" + "  \"messages\": [\n" + "    {\n"
				+ "      \"role\": \"system\",\n"
				+ "      \"content\": \"You are an AI assistant that provides suggestions for creating tasks and events in CRM based solely on the content of the input message. The content of the task or event, if any found, should only be from the input message. If no task or event suggestions are found in the input message, return empty data. Suggestions can either be both task and event list or only tasks or only events or empty. If task suggestions are found, they should include description and due date. Due Date format should be YYYY-MM-DD. If event suggestions are found, they should include description and start datetime and end datetime. If end datetime not provided it should be start datetime + 1 hour. Start datetime and end datetime format is yyyy-MM-dd'T'HH:mm:ss.SSS+0000. Today's date is "
				+ todayDate
				+ ". Use the functions provided to determine events and tasks suggestions. If no tasks or events are possible for the given input message, return an empty response. Example 1: Input Message: Had a call with the product team. They need a pitch deck by tomorrow. A meeting is gonna be held in 5 days to discuss the product. Output for Task: Description: Create pitch deck. Output for Event: Description: Meeting with product team to discuss the product. Example 2: Input Message: 3 tasks. Then it should return an empty response as no tasks and no events can be suggested from the input message.\"\n"
				+ "    },\n" + "    {\n" + "      \"role\": \"user\",\n" + "      \"content\": \" Input message: \\n"
				+ text + "\\n\"\n" + "    }\n" + "  ],\n" + "  \"functions\": [\n" + "    {\n"
				+ "      \"name\": \"suggest_actions\",\n"
				+ "      \"description\": \"This is a function for suggesting actions in CRM (example Salesforce, Freshsales) based on the input message.\",\n"
				+ "      \"parameters\": {\n" + "        \"type\": \"object\",\n" + "        \"properties\": {\n"
				+ "          \"add_task\": {\n" + "            \"name\": \"add_task\",\n"
				+ "            \"description\": \"Tasks using the input message. The task should be created only from the input message, and if no task can be generated from the input message, then empty data should be returned.\",\n"
				+ "            \"type\": \"array\",\n" + "            \"items\": {\n"
				+ "              \"type\": \"object\",\n" + "              \"properties\": {\n"
				+ "                \"description\": {\n" + "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"Description for the task to add.\"\n" + "                },\n"
				+ "                \"due_date\": {\n" + "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"Due date for the task in YYYY-MM-DD format. Today's date is "
				+ todayDate + ".\"\n" + "                }\n" + "              }\n" + "            }\n"
				+ "          },\n" + "          \"add_event\": {\n" + "            \"name\": \"add_event\",\n"
				+ "            \"description\": \"Events using the input message. The event should be created only from the input message, and if no event can be generated from the input message, then empty data should be returned.\",\n"
				+ "            \"type\": \"array\",\n" + "            \"items\": {\n"
				+ "              \"type\": \"object\",\n" + "              \"properties\": {\n"
				+ "                \"description\": {\n" + "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"Description for the event to add.\"\n" + "                },\n"
				+ "                \"start_datetime\": {\n" + "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"Start Datetime for the event in yyyy-MM-dd'T'HH:mm:ss.SSS+0000 format.\"\n"
				+ "                },\n" + "                \"end_datetime\": {\n"
				+ "                  \"type\": \"string\",\n"
				+ "                  \"description\": \"End Datetime for the event in yyyy-MM-dd'T'HH:mm:ss.SSS+0000 format.\"\n"
				+ "                }\n" + "              }\n" + "            }\n" + "          }\n" + "        }\n"
				+ "      }\n" + "    }\n" + "  ]\n" + "}";

		return payload;
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
