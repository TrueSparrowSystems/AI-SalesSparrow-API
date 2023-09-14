package com.salessparrow.api.unit.lib.salesforce.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.httpLib.HttpClient.HttpResponse;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceTokens;

@SpringBootTest
@Import({ SalesforceTokens.class })
public class SalesforceTokensTest {

	@MockBean
	private HttpClient httpClientMock;

	@Autowired
	private SalesforceTokens salesforceTokens;

	@Test
	public void testRevokeTokensSuccess() throws Exception {
		MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

		String instanceUrl = "https://example.com";
		String refreshToken = "your-refresh-token";

		HttpResponse expectedResponse = new HttpResponse();
		expectedResponse.setResponseBody("");

		httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), any(), anyInt()))
			.thenReturn(expectedResponse);

		HttpResponse response = salesforceTokens.revokeTokens(instanceUrl, refreshToken);

		assertEquals(expectedResponse, response);

		httpClientMockedStatic.close();
	}

	@Test
	public void testRevokeTokensFailure() throws Exception {
		MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class);

		String instanceUrl = "https://example.com";
		String refreshToken = "invalid-refresh-token";

		httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), any(), anyInt()))
			.thenThrow(new RuntimeException("Invalid refresh token"));

		assertThrows(CustomException.class, () -> {
			salesforceTokens.revokeTokens(instanceUrl, refreshToken);
		});
		httpClientMockedStatic.close();
	}

	@Test
	public void testGetTokensSuccess() throws Exception {
		try (MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class)) {

			String code = "dummyCode";
			String redirectUri = "dummyRedirectUri";

			Map<String, String> headers = new HashMap<>();
			headers.put("Authorization", "Dummy Bearer Header");

			String responseBody = "Mock Response Body";
			HttpResponse mockResponse = new HttpResponse();
			mockResponse.setResponseBody(responseBody);

			httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()))
				.thenReturn(mockResponse);

			HttpResponse actualResponse = salesforceTokens.getTokens(code, redirectUri, false);

			// Assertions
			assertEquals(mockResponse.getResponseBody(), actualResponse.getResponseBody());
			httpClientMockedStatic.verify(
					() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()), Mockito.times(1));
		}

	}

	@Test
	public void testGetTokensException() throws Exception {
		try (MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class)) {

			String code = "dummyCode";
			String redirectUri = "dummyRedirectUri";

			Map<String, String> headers = new HashMap<>();
			headers.put("Authorization", "Dummy Bearer Header");

			String responseBody = "Mock Response Body";
			HttpResponse mockResponse = new HttpResponse();
			mockResponse.setResponseBody(responseBody);

			httpClientMockedStatic.when(() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()))
				.thenThrow(new RuntimeException("Some error occurred"));

			CustomException exception = assertThrows(CustomException.class, () -> {
				salesforceTokens.getTokens(code, redirectUri, false);
			});

			// Assertions
			assertNotNull(exception);
			ParamErrorObject paramErrorObject = exception.getParamErrorObject();
			assertNotNull(paramErrorObject);
			assertEquals("l_s_w_sgt_gt_1", paramErrorObject.getInternalErrorIdentifier());

			httpClientMockedStatic.verify(
					() -> HttpClient.makePostRequest(anyString(), anyMap(), anyString(), anyInt()), Mockito.times(1));

		}
	}

}
