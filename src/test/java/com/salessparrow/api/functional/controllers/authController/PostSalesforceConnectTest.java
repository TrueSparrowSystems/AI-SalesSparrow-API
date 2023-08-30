package com.salessparrow.api.functional.controllers.authController;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetIdentity;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetTokens;
import com.salessparrow.api.services.salesforce.AuthService;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class PostSalesforceConnectTest {

	@Autowired
	private ResourceLoader resourceLoader;

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
	private SalesforceGetTokens mockGetTokens;

	@MockBean
	private SalesforceGetIdentity mockGetIdentity;

	@Mock
	private HttpResponse getTokensHttpMockResponse;

	@InjectMocks
	private AuthService authService;

	Logger logger = LoggerFactory.getLogger(PostSalesforceConnectTest.class);

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
	public void testPostSalesforceConnectSignup() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		List<Scenario> testDataItems = loadTestData(currentFunctionName);

		for (Scenario testDataItem : testDataItems) {
			logger.info("Running test scenario: " + testDataItem.getDescription());
			ObjectMapper objectMapper = new ObjectMapper();

			HttpResponse getTokensMockRes = new HttpResponse();
			getTokensMockRes.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("getTokens")));

			HttpResponse getIdentityMockRes = new HttpResponse();
			getIdentityMockRes
				.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("getIdentity")));

			when(mockGetTokens.getTokens(anyString(), anyString())).thenReturn(getTokensMockRes);
			when(mockGetIdentity.getUserIdentity(anyString(), anyString())).thenReturn(getIdentityMockRes);

			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/salesforce/connect")
				.content(objectMapper.writeValueAsString(testDataItem.getInput().get("body")))
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON));

			String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

			if (resultActions.andReturn().getResponse().getStatus() == 200) {
				assertEquals(objectMapper.writeValueAsString(testDataItem.getOutput()), actualOutput);
			}
			else if (resultActions.andReturn().getResponse().getStatus() == 400) {
				common.compareErrors(testDataItem, actualOutput);
			}
			else {
				assertEquals(testDataItem.getOutput().get("http_code"),
						resultActions.andReturn().getResponse().getStatus());
			}
		}
	}

	@Test
	public void testPostSalesforceConnectLogin() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/functional/controllers/authController/PostSalesforceConnectFixture.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		List<Scenario> testDataItems = loadTestData(currentFunctionName);

		for (Scenario testDataItem : testDataItems) {
			logger.info("Running test scenario: " + testDataItem.getDescription());
			ObjectMapper objectMapper = new ObjectMapper();

			HttpResponse getTokensMockRes = new HttpResponse();
			getTokensMockRes.setResponseBody(objectMapper.writeValueAsString(testDataItem.getMocks().get("getTokens")));

			when(mockGetTokens.getTokens(anyString(), anyString())).thenReturn(getTokensMockRes);

			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/salesforce/connect")
				.content(objectMapper.writeValueAsString(testDataItem.getInput().get("body")))
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON));

			String actualOutput = resultActions.andReturn().getResponse().getContentAsString();

			if (resultActions.andReturn().getResponse().getStatus() == 200) {
				assertEquals(objectMapper.writeValueAsString(testDataItem.getOutput()), actualOutput);
				verify(mockGetTokens, times(1)).getTokens(anyString(), anyString());
				verify(mockGetIdentity, times(0)).getUserIdentity(anyString(), anyString());
			}
			else if (resultActions.andReturn().getResponse().getStatus() == 400) {
				common.compareErrors(testDataItem, actualOutput);
			}
			else {
				assertEquals(testDataItem.getOutput().get("http_code"),
						resultActions.andReturn().getResponse().getStatus());
			}
		}

	}

	public List<Scenario> loadTestData(String key) throws IOException {
		String scenariosPath = "classpath:data/functional/controllers/authController/SalesforceConnect.scenarios.json";
		Resource resource = resourceLoader.getResource(scenariosPath);
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, List<Scenario>> scenariosMap = new HashMap<>();
		scenariosMap = objectMapper.readValue(resource.getInputStream(),
				new TypeReference<HashMap<String, List<Scenario>>>() {
				});
		return scenariosMap.get(key);
	}

}