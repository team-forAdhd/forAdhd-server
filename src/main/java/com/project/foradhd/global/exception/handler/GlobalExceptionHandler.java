package com.project.foradhd.global.exception.handler;

import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import com.project.foradhd.global.exception.SystemException;
import com.project.foradhd.global.exception.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("Exception occurred while processing request", e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(SystemException e) {
        Throwable cause = e.getCause();
        log.error("Exception occurred while processing request", cause);

        ErrorCode errorCode = ErrorCode.SYSTEM_ERROR;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
