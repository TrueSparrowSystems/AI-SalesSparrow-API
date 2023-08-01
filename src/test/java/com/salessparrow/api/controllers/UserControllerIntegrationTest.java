package com.salessparrow.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

import com.salessparrow.api.dto.UserDto;
import com.salessparrow.api.lib.globalConstants.CookieConstants;
import com.salessparrow.api.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SqlGroup({
        @Sql(value = "classpath:db/flush.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:db/user.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class UserControllerIntegrationTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    // @Test
    // public void testSignupEndpoint() throws Exception {
    //     // Create a sample UserDto object to be sent in the request body
    //     UserDto userDto = new UserDto("newuser@gmail.com", "password123");

    //     // Convert the UserDto object to JSON string
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     String requestJson = objectMapper.writeValueAsString(userDto);

    //     // Perform the signup request
    //     MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(requestJson))
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value("newuser@gmail.com"))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.user.password").doesNotExist()) // Ensure password is not returned in the response
    //             .andReturn();

    //     // Verify the response headers and body
    //     MockHttpServletResponse response = result.getResponse();
    //     assertThat(response.getHeader(HttpHeaders.SET_COOKIE)).isNotNull();

    //     assertThat(this.repository.findAll()).hasSize(2);
    // }

    @Test
    public void testLoginEndpoint() throws Exception {
        // Create a sample UserDto object to be sent in the request body
        UserDto userDto = new UserDto("123@gmail.com", "123");

        // Convert the UserDto object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value("123@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.password").doesNotExist()); // Ensure password is not returned in the response

        assertThat(this.repository.findAll()).hasSize(1);
    }

    @Test
    public void testLogoutEndpoint() throws Exception {
        // Create a sample UserDto object to be sent in the login request body
        UserDto userDto = new UserDto("123@gmail.com", "123");

        // Convert the UserDto object to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(userDto);

        // Perform user login and retrieve the cookie from the response
        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String cookieValue = loginResult.getResponse().getCookie(CookieConstants.USER_LOGIN_COOKIE_NAME).getValue();

        // Perform user logout using the obtained cookie
        mockMvc.perform(MockMvcRequestBuilders.post("/user/logout")
                .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue)))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verify that the cookie is not present in the response headers
                .andExpect(MockMvcResultMatchers.content().string("User logged out successfully"));
    }

    @Test
    public void testProfileEndpoint() throws Exception {
        // Perform login first to get the authentication token
        UserDto userDto = new UserDto("123@gmail.com", "123");
        ObjectMapper objectMapper = new ObjectMapper();
        String loginRequestJson = objectMapper.writeValueAsString(userDto);

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String cookieValue = loginResult.getResponse().getCookie(CookieConstants.USER_LOGIN_COOKIE_NAME).getValue();

        // Now, perform the request to the /profile endpoint using the obtained token
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile")
                .cookie(new Cookie(CookieConstants.USER_LOGIN_COOKIE_NAME, cookieValue))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value("123@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.password").doesNotExist());
    }
}
