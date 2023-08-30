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
import com.salessparrow.api.dto.entities.AddTaskSuggestionEntityDto;
import com.salessparrow.api.dto.formatter.CrmActionSuggestionsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.openAi.OpenAiPayloadBuilder;
import com.salessparrow.api.lib.openAi.OpenAiRequest;
import com.salessparrow.api.lib.validators.DateFormatValidator;

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

	private Logger logger = org.slf4j.LoggerFactory.getLogger(SuggestionsController.class);

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
			JsonNode argumentsNode = rootNode.get("choices")
				.get(0)
				.get("message")
				.get("function_call")
				.get("arguments");

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			String argumentsJson = objectMapper.convertValue(argumentsNode, String.class);

			Map<String, List<AddTaskSuggestionEntityDto>> arguments = objectMapper.readValue(argumentsJson,
					new TypeReference<Map<String, List<AddTaskSuggestionEntityDto>>>() {
					});
			List<AddTaskSuggestionEntityDto> addTaskList = arguments.get("add_task");

			List<AddTaskSuggestionEntityDto> formattedTaskSuggestionEntityDtos = new ArrayList<>();
			if (addTaskList != null) {
				for (AddTaskSuggestionEntityDto addTask : addTaskList) {
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

			crmActionSuggestionsFormatterDto.setAddTaskSuggestions(formattedTaskSuggestionEntityDtos);
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
		return input.replace("\"", "\\\"").replace("\n", "\\n");
	}

}
