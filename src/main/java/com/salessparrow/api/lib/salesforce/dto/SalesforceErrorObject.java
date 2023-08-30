package com.salessparrow.api.lib.salesforce.dto;

import lombok.Data;

@Data
public class SalesforceErrorObject {
    private boolean success;
    private String errorCode;
    private String message;
    private String salesforceErrorCode;

    public SalesforceErrorObject(boolean success, String errorCode, String message, String salesforceErrorCode) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.salesforceErrorCode = salesforceErrorCode;
    }
}
