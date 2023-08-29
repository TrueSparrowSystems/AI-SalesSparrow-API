package com.salessparrow.api.unit.lib.salesforce.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.lib.AwsKms;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.helper.SalesforceOAuthToken;
import com.salessparrow.api.lib.salesforce.wrappers.SalesforceGetRefreshedAccessToken;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*; 

public class SalesforceOAuthTokenTest {

  @Mock
  private AwsKms awsKms;

  @Mock
  private SalesforceOauthTokenRepository salesforceOauthTokenRepository;

  @Mock
  private SalesforceGetRefreshedAccessToken salesforceGetRefreshedAccessToken;

  @Mock
  private Util util;

  @InjectMocks
  private SalesforceOAuthToken salesforceOauthToken;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFetchAccessToken() {

    SalesforceOauthToken sfOAuthToken = new SalesforceOauthToken();
    sfOAuthToken.setAccessToken("encrypted_access_token");

    when(awsKms.decryptToken("encrypted_access_token")).thenReturn("decrypted_access_token");

    String decryptedAccessToken = salesforceOauthToken.fetchAccessToken(sfOAuthToken);

    assertEquals("decrypted_access_token", decryptedAccessToken);
  }

  @Test
  public void testUpdateAndGetRefreshedAccessToken() throws Exception {

    SalesforceOauthToken sfOAuthToken = new SalesforceOauthToken();
    sfOAuthToken.setRefreshToken("encrypted_refresh_token");

    String responseBody = "{\"access_token\":\"new_access_token\"}";
    ObjectMapper map = new ObjectMapper();  
    JsonNode responseBodyNode = map.readTree(responseBody); 

    when(awsKms.decryptToken("encrypted_refresh_token")).thenReturn("decrypted_refresh_token");

    HttpClient.HttpResponse response = new HttpClient.HttpResponse(200, responseBody, null, "application/json");
    when(salesforceGetRefreshedAccessToken.getRefreshedAccessToken("decrypted_refresh_token")).thenReturn(response);

    when(util.getJsonNode(responseBody)).thenReturn(responseBodyNode);

    when(awsKms.encryptToken("new_access_token")).thenReturn("encrypted_access_token");

    when(salesforceOauthTokenRepository.saveSalesforceOauthToken(any(SalesforceOauthToken.class)))
      .thenAnswer(invocation -> invocation.getArgument(0)); // Return the argument back

    String decryptedAccessToken = salesforceOauthToken.updateAndGetRefreshedAccessToken(sfOAuthToken);

    assertEquals("new_access_token", decryptedAccessToken);
  }
}

