package com.project.foradhd.global.exception.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.foradhd.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode,
                                                    List<ValidationErrorResponse> validationErrors) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .validationErrors(validationErrors)
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
