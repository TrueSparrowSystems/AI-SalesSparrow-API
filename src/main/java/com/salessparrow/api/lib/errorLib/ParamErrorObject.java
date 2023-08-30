package com.salessparrow.api.lib.errorLib;

import java.util.List;

import lombok.Data;

@Data
public class ParamErrorObject {

	private String internalErrorIdentifier;

	private String message;

	private List<String> paramErrorIdentifiers;

	public ParamErrorObject() {
	}

	public ParamErrorObject(String internalErrorIdentifier, String message, List<String> paramErrorIdentifiers) {
		this.internalErrorIdentifier = internalErrorIdentifier;
		this.message = message;
		this.paramErrorIdentifiers = paramErrorIdentifiers;
	}

}
