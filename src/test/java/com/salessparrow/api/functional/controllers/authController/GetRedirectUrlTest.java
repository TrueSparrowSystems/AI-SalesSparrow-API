package com.salessparrow.api.functional.controllers.authController;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class })
public class GetRedirectUrlTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Common common;

	@Test
	public void getRedirectUrl() throws Exception {
		List<Scenario> testDataItems = common
			.loadScenariosData("classpath:data/functional/controllers/authController/redirectUrl.scenarios.json");

		for (Scenario testDataItem : testDataItems) {
			ObjectMapper objectMapper = new ObjectMapper();
			String expectedOutput = objectMapper.writeValueAsString(testDataItem.getOutput());

			ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/auth/salesforce/redirect-url")
					.param("redirect_uri", (String) testDataItem.getInput().get("redirect_uri"))
					.param("state", (String) testDataItem.getInput().get("state"))
					.contentType(MediaType.APPLICATION_JSON));

			String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
			if (resultActions.andReturn().getResponse().getStatus() == 200) {
				assertEquals(expectedOutput, contentAsString);
			}
			else {
				common.compareErrors(testDataItem, contentAsString);
			}
		}
	}

}
