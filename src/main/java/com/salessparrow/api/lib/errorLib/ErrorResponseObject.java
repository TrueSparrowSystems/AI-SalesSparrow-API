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
    List<ParamErrorConfig> paramErrors;

    public ErrorResponseObject() {
    }

    public ErrorResponseObject(int httpCode, String message, String code, String internalErrorIdentifier,
            List<ParamErrorConfig> paramErrors) {
        this.httpCode = httpCode;
        this.message = message;
        this.code = code;
        this.internalErrorIdentifier = internalErrorIdentifier;
        this.paramErrors = paramErrors;
    }

    @Override
    public String toString() {
        return "ErrorResponseObject [code=" + code + ", paramErrors=" + paramErrors + ", httpCode=" + httpCode
                + ", internalErrorIdentifier=" + internalErrorIdentifier + ", message=" + message + "]";
    }
}
