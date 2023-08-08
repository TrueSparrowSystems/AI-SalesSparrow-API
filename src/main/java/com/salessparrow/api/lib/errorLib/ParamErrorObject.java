package com.salessparrow.api.lib.errorLib;

import java.util.List;

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

    public String getInternalErrorIdentifier() {
        return internalErrorIdentifier;
    }

    public void setInternalErrorIdentifier(String internalErrorIdentifier) {
        this.internalErrorIdentifier = internalErrorIdentifier;
    }

    public List<String> getParamErrorIdentifiers() {
        return paramErrorIdentifiers;
    }

    public void setParamErrorIdentifiers(List<String> paramErrorIdentifiers) {
        this.paramErrorIdentifiers = paramErrorIdentifiers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
