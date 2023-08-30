package com.salessparrow.api.functional.controllers.authController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceTokens;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class PostDisconnectTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Common common;

	@Autowired
	private Setup setup;

	@Autowired
	private Cleanup cleanup;

	@Autowired
	private LoadFixture loadFixture;

	@MockBean
	private SalesforceTokens mockGetTokens;

	@BeforeEach
	public void setUp() throws DynamobeeException, IOException {
		MockitoAnnotations.openMocks(this);
		setup.perform();
	}

	@AfterEach
	public void tearDown() {
		cleanup.perform();
	}

	@Test
	public void testPostDisconnectSuccess() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/functional/controllers/authController/PostDisconnectFixture.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		List<Scenario> testDataItems = common.loadScenariosData(
				"classpath:data/functional/controllers/authController/Disconnect.scenarios.json", currentFunctionName);

		for (Scenario testDataItem : testDataItems) {
			ObjectMapper objectMapper = new ObjectMapper();
			String cookieValue = Constants.SALESFORCE_ACTIVE_USER_COOKIE_VALUE;

			HttpResponse getTokensMockRes = new HttpResponse();
			getTokensMockRes
				.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("revokeTokens")));

			when(mockGetTokens.getTokens(anyString(), anyString())).thenReturn(getTokensMockRes);

			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/disconnect")
				.cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
				.contentType(MediaType.APPLICATION_JSON));

			String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

			if (testDataItem.getOutput().get("success").equals(true)) {
				assertEquals(204, resultActions.andReturn().getResponse().getStatus());
			}
			else {
				common.compareErrors(testDataItem, actualOutput);
			}
		}
	}

	@Test
	public void testPostDisconnectNoTokens() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/functional/controllers/authController/PostDisconnectFixture.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		List<Scenario> testDataItems = common.loadScenariosData(
				"classpath:data/functional/controllers/authController/Disconnect.scenarios.json", currentFunctionName);

		for (Scenario testDataItem : testDataItems) {
			ObjectMapper objectMapper = new ObjectMapper();
			String cookieValue = Constants.SALESFORCE_ACTIVE_USER_COOKIE_VALUE;

			HttpResponse getTokensMockRes = new HttpResponse();
			getTokensMockRes
				.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("revokeTokens")));

			when(mockGetTokens.getTokens(anyString(), anyString())).thenReturn(getTokensMockRes);

			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/disconnect")
				.cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
				.contentType(MediaType.APPLICATION_JSON));

			String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

			if (resultActions.andReturn().getResponse().getStatus() != 204) {
				common.compareErrors(testDataItem, actualOutput);
			}
		}
	}

}
