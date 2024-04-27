package com.project.foradhd.global.exception.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    @Builder.Default
    private String timestamp = LocalDateTime.now().toString();
    private Integer status;
    private String code;
    private String message;
}
