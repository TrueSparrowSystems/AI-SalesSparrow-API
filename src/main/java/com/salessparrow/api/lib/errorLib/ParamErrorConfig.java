package com.salessparrow.api.lib.errorLib;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ParamErrorConfig {
    private String parameter;
    private String paramErrorIdentifier;
    private String message;

    public ParamErrorConfig() {
    }

    public ParamErrorConfig(String parameter, String paramErrorIdentifier, String message) {
        this.parameter = parameter;
        this.paramErrorIdentifier = paramErrorIdentifier;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "parameter='" + parameter + '\'' +
                ", paramErrorIdentifier='" + paramErrorIdentifier + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
