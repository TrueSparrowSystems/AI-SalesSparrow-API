package com.salessparrow.api.lib.salesforce.dto;

import lombok.Data;

@Data
public class CompositeRequestDto {

	private String method;

	private String url;

	private String referenceId;

	private Object body;

	public CompositeRequestDto(String method, String url, String referenceId) {
		this.method = method;
		this.url = url;
		this.referenceId = referenceId;
	}

	public CompositeRequestDto(String method, String url, String referenceId, Object body) {
		this.method = method;
		this.url = url;
		this.referenceId = referenceId;
		this.body = body;
	}

}
