package com.salessparrow.api.unit.lib.salesforce.helper;

import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.salesforce.helper.SalesforceOAuthToken;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceRequestInterface;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SalesforceRequestTest {

	@Mock
	private SalesforceOAuthToken salesforceOAuthToken;

	@Mock
	private SalesforceOauthTokenRepository sfOauthTokenRepository;

	@InjectMocks
	private SalesforceRequest salesforceRequest;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testMakeRequestSuccess() {

		SalesforceOauthToken sfOAuthToken = new SalesforceOauthToken();
		sfOAuthToken.setAccessToken("access_token");
		sfOAuthToken.setInstanceUrl("https://instance.url");

		when(sfOauthTokenRepository.getSalesforceOauthTokenByExternalUserId("user123")).thenReturn(sfOAuthToken);

		// Mock SalesforceOAuthToken
		when(salesforceOAuthToken.fetchAccessToken(any(SalesforceOauthToken.class))).thenReturn("access_token");

		// Mock SalesforceRequestInterface
		SalesforceRequestInterface<String> requestInterface = (token, instanceUrl) -> "Response";

		// Test the method
		String response = salesforceRequest.makeRequest("user123", requestInterface);

		// Verify that the methods were called with the expected parameters
		verify(sfOauthTokenRepository).getSalesforceOauthTokenByExternalUserId("user123");
		verify(salesforceOAuthToken).fetchAccessToken(sfOAuthToken);

		// Verify the response
		assertEquals("Response", response);
	}

	@Test
	public void testMakeRequestWebClientResponseException() {

		SalesforceOauthToken sfOAuthToken = new SalesforceOauthToken();
		sfOAuthToken.setAccessToken("access_token");
		sfOAuthToken.setInstanceUrl("https://instance.url");

		when(sfOauthTokenRepository.getSalesforceOauthTokenByExternalUserId("user123")).thenReturn(sfOAuthToken);

		// Mock SalesforceOAuthToken
		when(salesforceOAuthToken.fetchAccessToken(any(SalesforceOauthToken.class))).thenReturn("access_token");

		// Mock SalesforceRequestInterface
		SalesforceRequestInterface<String> requestInterface = (token, instanceUrl) -> {
			throw new WebClientResponseException(401, "Unauthorized", null, null, null);
		};

		// Test the method and expect a CustomException
		assertThrows(CustomException.class, () -> salesforceRequest.makeRequest("user123", requestInterface));

		// Verify that the methods were called with the expected parameters
		verify(sfOauthTokenRepository).getSalesforceOauthTokenByExternalUserId("user123");
		verify(salesforceOAuthToken).fetchAccessToken(sfOAuthToken);

		// Verify that the updateAndGetRefreshedAccessToken method was called once
		verify(salesforceOAuthToken, times(1)).updateAndGetRefreshedAccessToken(sfOAuthToken);
	}

	@Test
	public void testMakeRequestInternalServerErrorException() {

		SalesforceOauthToken sfOAuthToken = new SalesforceOauthToken();
		sfOAuthToken.setAccessToken("access_token");
		sfOAuthToken.setInstanceUrl("https://instance.url");

		when(sfOauthTokenRepository.getSalesforceOauthTokenByExternalUserId("user123")).thenReturn(sfOAuthToken);

		// Mock SalesforceOAuthToken
		when(salesforceOAuthToken.fetchAccessToken(any(SalesforceOauthToken.class))).thenReturn("access_token");

		// Mock SalesforceRequestInterface
		SalesforceRequestInterface<String> requestInterface = (token, instanceUrl) -> {
			throw new WebClientResponseException(500, "Internal Server Error", null, null, null);
		};

		// Test the method and expect a CustomException
		assertThrows(CustomException.class, () -> salesforceRequest.makeRequest("user123", requestInterface));

		// Verify that the methods were called with the expected parameters
		verify(sfOauthTokenRepository).getSalesforceOauthTokenByExternalUserId("user123");
		verify(salesforceOAuthToken).fetchAccessToken(sfOAuthToken);

		// Verify that the updateAndGetRefreshedAccessToken method was not called
		verify(salesforceOAuthToken, never()).updateAndGetRefreshedAccessToken(sfOAuthToken);
	}

}
