package com.salessparrow.api.lib.errorLib;

import java.util.List;

public class ErrorResponseObject {
    int http_code;
    String message;
    String code;
    String internal_error_identifier;
    List<ParamErrorConfig> param_errors;

    public ErrorResponseObject() {
    }

    public ErrorResponseObject(int httpCode, String message, String code, String internalErrorIdentifier,
            List<ParamErrorConfig> errorData) {
        this.http_code = httpCode;
        this.message = message;
        this.code = code;
        this.internal_error_identifier = internalErrorIdentifier;
        this.param_errors = errorData;
    }

    public int getHttpCode() {
        return http_code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getInternalErrorIdentifier() {
        return internal_error_identifier;
    }

    public List<ParamErrorConfig> getErrorData() {
        return param_errors;
    }

    public void setHttpCode(int httpCode) {
        this.http_code = httpCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInternalErrorIdentifier(String internalErrorIdentifier) {
        this.internal_error_identifier = internalErrorIdentifier;
    }

    public void setErrorData(List<ParamErrorConfig> errorData) {
        this.param_errors = errorData;
    }

    @Override
    public String toString() {
        return "ErrorResponseObject [code=" + code + ", errorData=" + param_errors + ", httpCode=" + http_code
                + ", internalErrorIdentifier=" + internal_error_identifier + ", message=" + message + "]";
    }
}
