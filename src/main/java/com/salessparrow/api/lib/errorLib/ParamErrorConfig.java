package com.salessparrow.api.lib.errorLib;

import lombok.Data;

@Data
public class ParamErrorConfig {
    private String parameter;
    private String message;

    public ParamErrorConfig() {
    }

    public ParamErrorConfig(String parameter, String message) {
        this.parameter = parameter;
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
