package com.salessparrow.api.unit.globalConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;

@SpringBootTest
public class SalesforceConstantsTest {

    @Test
    void testCompositeUrlPath() {
        assertEquals("/services/data/v58.0/composite", new SalesforceConstants().compositeUrlPath());
    }

    @Test
    void testQueryUrlPath() {
        assertEquals("/services/data/v58.0/query/?q=", new SalesforceConstants().queryUrlPath());
    }

    @Test
    void testSObjectsPath() {
        assertEquals("/services/data/v58.0/sobjects", new SalesforceConstants().sObjectsPath());
    }

    @Test
    void testSalesforceCompositeUrl() {
        assertEquals("https://test.salesforce.com/services/data/v58.0/composite", new SalesforceConstants().salesforceCompositeUrl("https://test.salesforce.com"));
    }

    @Test
    void testOauth2AuthorizeUrl() {
        assertEquals("https://test.salesforce.com/services/oauth2/authorize", new SalesforceConstants().oauth2AuthorizeUrl());
    }

    @Test
    void testOauth2Url() {
        assertEquals("https://test.salesforce.com/services/oauth2/token", new SalesforceConstants().oauth2Url());
    }

    @Test
    void testSalesforceCreateNoteUrl() {
        assertEquals("/services/data/v58.0/sobjects/ContentNote", new SalesforceConstants().salesforceCreateNoteUrl());
    }

    @Test
    void testSalesforceAttachNoteUrl() {
        assertEquals("/services/data/v58.0/sobjects/ContentDocumentLink", new SalesforceConstants().salesforceAttachNoteUrl());
    }

    @Test
    void testIdentityUrl() {
        assertEquals("/services/oauth2/userinfo", new SalesforceConstants().identityUrl());
    }

    @Test
    void testAuthorizationCodeGrantType() {
        assertEquals("authorization_code", new SalesforceConstants().authorizationCodeGrantType());
    }

    @Test
    void testRefreshTokenGrantType() {
        assertEquals("refresh_token", new SalesforceConstants().refreshTokenGrantType());
    }

    @Test
    void testTimeoutMillis() {
        assertEquals(10000, new SalesforceConstants().timeoutMillis());
    }

    @Test
    void testSalesforceNotesContentUrl() {
        assertEquals("https://test.salesforce.com/services/data/v58.0/sobjects/ContentNote/123/Content", new SalesforceConstants().salesforceNotesContentUrl("https://test.salesforce.com", "123"));
    }

    @Test
    void testSalesforceCreateTaskUrl() {
        assertEquals("/services/data/v58.0/sobjects/Task", new SalesforceConstants().salesforceCreateTaskUrl());
    }
}
