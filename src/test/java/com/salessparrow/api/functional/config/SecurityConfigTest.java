package com.salessparrow.api.functional.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.io.IOException;
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

import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class SecurityConfigTest {

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
	public void allRoutesShouldBeAccessibleWithoutAuthenticationAndAuthorization() throws Exception {
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/salesforce/redirect-url")
			.param("redirect_uri", "http://localhost:3000")
			.contentType(MediaType.APPLICATION_JSON));
		assertEquals(resultActions.andReturn().getResponse().getStatus(), 200);
	}

	@Test
	public void securityHeadersShouldBeSet() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/auth/salesforce/redirect-url")
				.param("redirect_uri", "http://localhost:3000")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(header().string("X-Content-Type-Options", "nosniff"))
			.andExpect(header().string("X-XSS-Protection", "1; mode=block"))
			.andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
			.andExpect(header().string("Pragma", "no-cache"))
			.andExpect(header().string("Expires", "0"))
			.andExpect(header().string("X-Frame-Options", "DENY"))
			.andExpect(header().string("Referrer-Policy", "same-origin"))
			.andExpect(header().string("Content-Type", "application/json"));
	}

}
