package com.salessparrow.api.lib.errorLib;

public class ParamErrorConfig {
    private String parameter;
    private String message;

    public ParamErrorConfig() {
    }

    public ParamErrorConfig(String parameter, String message) {
        this.parameter = parameter;
        this.message = message;
    }

    public String getParameter() {
        return parameter;
    }

    public String getMessage() {
        return message;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "parameter='" + parameter + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
