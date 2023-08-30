package com.salessparrow.api.functional.interceptors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class JsonOnlyInterceptorTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Setup setup;

	@Autowired
	private Cleanup cleanup;

	@Autowired
	private Common common;

	@BeforeEach
	public void setUp() throws DynamobeeException, IOException {
		setup.perform();
	}

	@AfterEach
	public void tearDown() {
		cleanup.perform();
	}

	@Test
	public void preHandle() throws Exception {
		List<Scenario> testDataItems = common
			.loadScenariosData("classpath:data/functional/interceptors/jsonOnlyInterceptor.scenarios.json");
		for (Scenario testDataItem : testDataItems) {
			ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/auth/salesforce/redirect-url")
					.contentType(MediaType.ALL_VALUE));

			String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
			common.compareErrors(testDataItem, contentAsString);
		}

	}

}
