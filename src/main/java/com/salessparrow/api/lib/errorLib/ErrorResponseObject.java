package com.salessparrow.api.lib.errorLib;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponseObject {
    int httpCode;
    String message;
    String code;
    String internalErrorIdentifier;
    List<ParamErrorConfig> param_errors;

    public ErrorResponseObject() {
    }

    public ErrorResponseObject(int httpCode, String message, String code, String internalErrorIdentifier,
            List<ParamErrorConfig> errorData) {
        this.httpCode = httpCode;
        this.message = message;
        this.code = code;
        this.internalErrorIdentifier = internalErrorIdentifier;
        this.param_errors = errorData;
    }

    @Override
    public String toString() {
        return "ErrorResponseObject [code=" + code + ", errorData=" + param_errors + ", httpCode=" + httpCode
                + ", internalErrorIdentifier=" + internalErrorIdentifier + ", message=" + message + "]";
    }
}
