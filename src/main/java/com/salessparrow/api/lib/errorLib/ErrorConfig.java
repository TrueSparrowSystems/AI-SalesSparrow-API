package com.salessparrow.api.lib.errorLib;

import lombok.Data;

@Data
public class ErrorConfig {

	private String httpCode;

	private String code;

	private String message;

	@Override
	public String toString() {
		return "ErrorInfo{" + "http_code='" + httpCode + '\'' + ", code='" + code + '\'' + ", message='" + message
				+ '\'' + '}';
	}

}