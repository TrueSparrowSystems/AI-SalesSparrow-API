package com.salessparrow.api.lib.salesforce.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompositeResponseDto {
    List<CompositeResponseBody> body;

    @Data
    public static class CompositeResponseBody {
        private String errorCode;
        private String message;
    }
}
