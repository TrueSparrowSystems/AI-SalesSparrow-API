package com.salessparrow.api.lib.globalConstants;

import org.springframework.stereotype.Component;
import com.salessparrow.api.config.CoreConstants;

@Component
public class SalesforceConstants {

	public String compositeUrlPath() {
		return "/services/data/v58.0/composite";
	}

	public String queryUrlPath() {
		return "/services/data/v58.0/query/?q=";
	}

	public String sObjectsPath() {
		return "/services/data/v58.0/sobjects";
	}

	public String salesforceCompositeUrl(String urlPrefix) {
		return urlPrefix + compositeUrlPath();
	}

	public String oauth2AuthorizeUrl() {
		return CoreConstants.salesforceAuthUrl() + "/services/oauth2/authorize";
	}

	public String oauth2Url() {
		return CoreConstants.salesforceAuthUrl() + "/services/oauth2/token";
	}

	public String revokeTokensUrl() {
		return "/services/oauth2/revoke";
	}

	public String salesforceCreateNoteUrl() {
		return sObjectsPath() + "/ContentNote";
	}

	public String salesforceCreateEventUrl() {
		return sObjectsPath() + "/Event";
	}

	public String salesforceDeleteNoteUrl(String noteId) {
		return sObjectsPath() + "/ContentNote/" + noteId;
	}

	public String salesforceAttachNoteUrl() {
		return sObjectsPath() + "/ContentDocumentLink";
	}

	public String identityUrl() {
		return "/services/oauth2/userinfo";
	}

	public String authorizationCodeGrantType() {
		return "authorization_code";
	}

	public String passwordGrantType() {
		return "password";
	}

	public String refreshTokenGrantType() {
		return "refresh_token";
	}

	public Integer timeoutMillis() {
		return 10000;
	}

	public String salesforceNotesContentUrl(String urlPrefix, String noteId) {
		return urlPrefix + "/services/data/v58.0/sobjects/ContentNote/" + noteId + "/Content";
	}

	public String salesforceCreateTaskUrl() {
		return sObjectsPath() + "/Task";
	}

	public String salesforceDeleteAccountTaskUrl(String taskId) {
		return sObjectsPath() + "/Task/" + taskId;
	}

	public String salesforceDeleteAccountEventUrl(String eventId) {
		return sObjectsPath() + "/Event/" + eventId;
	}

}
