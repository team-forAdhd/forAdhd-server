package com.project.foradhd.global.exception.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @Builder.Default
    private String timestamp = LocalDateTime.now().toString();
    private Integer status;
    private String code;
    private String message;
    private List<ValidationErrorResponse> validationErrors;

    @Getter
    @AllArgsConstructor
    public static class ValidationErrorResponse {

        private String fieldName;
        private String message;
    }
}
