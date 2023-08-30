package com.salessparrow.api.functional.controllers.suggestionsController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.Constants;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.openAi.OpenAiRequest;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class PostCrmActionsSuggestionsTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Setup setup;

	@Autowired
	private Cleanup cleanup;

	@Autowired
	private Common common;

	@Autowired
	private LoadFixture loadFixture;

	@MockBean
	private OpenAiRequest openAiRequestMock;

	@BeforeEach
	public void setUp() throws DynamobeeException, IOException {
		setup.perform();
	}

	@AfterEach
	public void tearDown() {
		cleanup.perform();
	}

	@Test
	public void testPostCrmActionsSuggestions() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/functional/controllers/suggestionsController/postCrmActionsSuggestions.fixtures.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		List<Scenario> testDataItems = common.loadScenariosData(
				"classpath:data/functional/controllers/suggestionsController/crmActionsSuggestions.scenarios.json");
		for (Scenario testDataItem : testDataItems) {
			ObjectMapper objectMapper = new ObjectMapper();
			HttpResponse getAccountMockResponse = new HttpResponse();
			getAccountMockResponse
				.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("makeRequest")));
			when(openAiRequestMock.makeRequest(any())).thenReturn(getAccountMockResponse);

			String expectedOutput = objectMapper.writeValueAsString(testDataItem.getOutput());
			String cookieValue = Constants.SALESFORCE_ACTIVE_USER_COOKIE_VALUE;

			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/suggestions/crm-actions")
				.content(objectMapper.writeValueAsString(testDataItem.getInput()))
				.cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
				.contentType(MediaType.APPLICATION_JSON));

			String actualOutput = resultActions.andReturn().getResponse().getContentAsString();
			if (resultActions.andReturn().getResponse().getStatus() == 200) {
				assertEquals(expectedOutput, actualOutput);
			}
			else {
				common.compareErrors(testDataItem, actualOutput);
			}
		}
	}

}
