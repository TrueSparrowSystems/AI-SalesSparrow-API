package com.salessparrow.api.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.controllers.SuggestionsController;
import com.salessparrow.api.dto.entities.AddEventSuggestionEntityDto;
import com.salessparrow.api.dto.entities.AddTaskSuggestionEntityDto;
import com.salessparrow.api.dto.formatter.CrmActionSuggestionsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.openAi.OpenAiPayloadBuilder;
import com.salessparrow.api.lib.openAi.OpenAiRequest;
import com.salessparrow.api.lib.validators.DateFormatValidator;
import com.salessparrow.api.lib.validators.DatetimeFormatValidator;

/**
 * GetCrmActionSuggestions is a class for getting the crm action suggestions.
 */
@Component
public class GetCrmActionSuggestions {

	@Autowired
	private OpenAiRequest openAiRequest;

	@Autowired
	private OpenAiPayloadBuilder openAiPayloadBuilder;

	private DateFormatValidator dateFormatValidator = new DateFormatValidator();

	private DatetimeFormatValidator datetimeFormatValidator = new DatetimeFormatValidator();

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SuggestionsController.class);

	/**
	 * Get the crm action suggestions.
	 * @param text
	 * @return
	 */
	public CrmActionSuggestionsFormatterDto getTaskSuggestions(String text) {
		logger.info("Crm actions suggestions lib called for text: " + text);

		String escapedText = escapeForJson(text);
		String payload = openAiPayloadBuilder.payloadForCrmActionsSuggestions(escapedText);

		String response = openAiRequest.makeRequest(payload).getResponseBody();

		return parseResponse(response);
	}

	/**
	 * Parse the response from openai.
	 * @param responseBody
	 * @return
	 */
	private CrmActionSuggestionsFormatterDto parseResponse(String responseBody) {
		CrmActionSuggestionsFormatterDto crmActionSuggestionsFormatterDto = new CrmActionSuggestionsFormatterDto();

		try {
			Util util = new Util();

			JsonNode rootNode = util.getJsonNode(responseBody);

			JsonNode functionNode = rootNode.get("choices").get(0).get("message").get("function_call");

			List<AddTaskSuggestionEntityDto> formattedTaskSuggestionEntityDtos = new ArrayList<>();
			List<AddEventSuggestionEntityDto> formattedEventSuggestionEntityDtos = new ArrayList<>();

			if (functionNode != null) {

				JsonNode argumentsNode = functionNode.get("arguments");

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				String argumentsJson = objectMapper.convertValue(argumentsNode, String.class);

				Map<String, List<Object>> arguments = objectMapper.readValue(argumentsJson,
						new TypeReference<Map<String, List<Object>>>() {
						});

				List<Object> addTaskList = arguments.get("add_task");
				List<Object> addEventList = arguments.get("add_event");

				List<AddTaskSuggestionEntityDto> typedAddTaskList = objectMapper.convertValue(addTaskList,
						new TypeReference<List<AddTaskSuggestionEntityDto>>() {
						});

				List<AddEventSuggestionEntityDto> typedAddEventList = objectMapper.convertValue(addEventList,
						new TypeReference<List<AddEventSuggestionEntityDto>>() {
						});

				if (typedAddTaskList != null) {
					for (AddTaskSuggestionEntityDto addTask : typedAddTaskList) {
						AddTaskSuggestionEntityDto addTaskSuggestionEntityDto = new AddTaskSuggestionEntityDto();
						addTaskSuggestionEntityDto.setDescription(addTask.getDescription());

						// Format the response check if duedate format is YYYY-MM-DD else
						// remove duedate
						String dueDate = addTask.getDueDate();
						if (dateFormatValidator.isValid(dueDate, null)) {
							addTaskSuggestionEntityDto.setDueDate(dueDate);
						}

						formattedTaskSuggestionEntityDtos.add(addTaskSuggestionEntityDto);
					}
				}

				if (typedAddEventList != null) {
					for (AddEventSuggestionEntityDto addEvent : typedAddEventList) {
						AddEventSuggestionEntityDto addEventSuggestionEntityDto = new AddEventSuggestionEntityDto();
						addEventSuggestionEntityDto.setDescription(addEvent.getDescription());

						// Format the response check if datetime format is
						// yyyy-MM-dd'T'HH:mm:ss.SSSZ else
						// remove datetime
						if (datetimeFormatValidator.isValid(addEvent.getStartDatetime(), null)) {
							addEventSuggestionEntityDto.setStartDatetime(addEvent.getStartDatetime());
						}

						if (datetimeFormatValidator.isValid(addEvent.getEndDatetime(), null)) {
							addEventSuggestionEntityDto.setEndDatetime(addEvent.getEndDatetime());
						}

						formattedEventSuggestionEntityDtos.add(addEventSuggestionEntityDto);
					}
				}
			}

			crmActionSuggestionsFormatterDto.setAddTaskSuggestions(formattedTaskSuggestionEntityDtos);
			crmActionSuggestionsFormatterDto.setAddEventSuggestions(formattedEventSuggestionEntityDtos);
			return crmActionSuggestionsFormatterDto;
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("l_gcas_p_1", "something_went_wrong", e.getMessage()));
		}
	}

	/**
	 * Escape the input string for json.
	 * @param input
	 * @return
	 */
	private String escapeForJson(String input) {
		return input.replace("\\", "\\\\") // Escape backslashes
			.replace("\"", "\\\"") // Escape double quotes
			.replace("\n", "\\n") // Escape newlines
			.replace("\r", "\\r") // Escape carriage returns
			.replace("\t", "\\t") // Escape tabs
			.replace("\b", "\\b") // Escape backspace
			.replace("\f", "\\f") // Escape form feeds
			.replaceAll("[\u0000-\u001F]", ""); // Replace control characters with empty
												// string
	}

}