package com.salessparrow.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testSalesforceRedirectUrlEndpoint() throws Exception{
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/salesforce/redirect-url")
              .contentType(MediaType.APPLICATION_JSON)
              .param("redirect_uri", "https://redirect.uri")
              .param("state", "state"))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.url").exists());
  }
}
 