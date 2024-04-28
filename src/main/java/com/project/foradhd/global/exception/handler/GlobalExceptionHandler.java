package com.project.foradhd.global.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import com.project.foradhd.global.exception.InternalSystemException;
import com.project.foradhd.global.exception.dto.response.ErrorResponse;
import com.project.foradhd.global.exception.dto.response.ErrorResponse.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_INVALID_FORMAT_ERROR_MESSAGE_TEMPLATE = "Cannot deserialize value of type `%s` from String \"%s\"";
    private static final String ENUM_INVALID_FORMAT_ERROR_MASSAGE_TEMPLATE = ": not one of the values accepted for Enum class: %s";
    private static final String DATE_TIME_INVALID_FORMAT_ERROR_MESSAGE_TEMPLATE = ": Failed to deserialize %s: (%s) Text '%s' could not be parsed at index %d";

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

    @ExceptionHandler(InternalSystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(InternalSystemException e) {
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
        log.error("Invalid request content", e);
        List<ValidationErrorResponse> validationErrors = e.getAllErrors().stream()
                .map(objectError -> {
                    if (objectError instanceof FieldError fieldError) {
                        return new ValidationErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
                    }
                    return new ValidationErrorResponse(objectError.getObjectName(), objectError.getDefaultMessage());
                })
                .toList();

        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .validationErrors(validationErrors)
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, 
                                                                HttpStatusCode status, WebRequest request) {
        log.error("Invalid request content", e);
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;

        //Check if the exception is due to JSON parsing error
        if (e.getCause() instanceof InvalidFormatException cause) {
            Object value = cause.getValue();
            Class<?> targetType = cause.getTargetType();
            String fieldName = cause.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining(", "));
            String message = generateValidationMessage(value, targetType, cause);

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(errorCode.getStatus().value())
                    .code(errorCode.name())
                    .message(errorCode.getMessage())
                    .validationErrors(List.of(
                            new ValidationErrorResponse(fieldName, message)))
                    .build();
            return new ResponseEntity<>(errorResponse, errorCode.getStatus());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    private String generateValidationMessage(Object value, Class<?> targetType, Throwable throwable) {
        String defaultMessage = String.format(DEFAULT_INVALID_FORMAT_ERROR_MESSAGE_TEMPLATE, targetType.getSimpleName(), value);
        if (targetType.isEnum()) {
            String enumMessage = String.format(
                    ENUM_INVALID_FORMAT_ERROR_MASSAGE_TEMPLATE,
                    Arrays.toString(targetType.getEnumConstants()));
            return defaultMessage + enumMessage;
        }
        if (throwable.getCause() instanceof DateTimeParseException cause) {
            String dateTimeMessage = String.format(
                    DATE_TIME_INVALID_FORMAT_ERROR_MESSAGE_TEMPLATE,
                    targetType.getTypeName(), cause.getClass().getTypeName(), value, cause.getErrorIndex());
            return defaultMessage + dateTimeMessage;
        }
        return defaultMessage;
    }
}
