package com.salessparrow.api.lib.errorLib;

import lombok.Data;

@Data
public class ErrorObject {

	private String internalErrorIdentifier;

	private String apiErrorIdentifier;

	private String message;

	public ErrorObject() {
	}

	public ErrorObject(String internalErrorIdentifier, String apiErrorIdentifier, String message) {
		this.internalErrorIdentifier = internalErrorIdentifier;
		this.apiErrorIdentifier = apiErrorIdentifier;
		this.message = message;
	}

}
