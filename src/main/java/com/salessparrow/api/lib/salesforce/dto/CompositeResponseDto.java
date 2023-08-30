package com.salessparrow.api.lib.salesforce.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompositeResponseDto {
    List<compositeResponse> compositeResponse;
    
    @Data
    public static class compositeResponse {
        private List<Body> body;
        private int httpStatusCode;
        private String referenceId;
    }

    @Data
    public static class Body {
        private String errorCode;
        private String message;
    }
}
