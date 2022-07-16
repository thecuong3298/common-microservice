package com.common.rest.error;

import com.common.dto.ResponseWrapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.common.rest.response.CommonErrorCode.BAD_REQUEST;
import static com.common.rest.response.CommonErrorCode.SYSTEM_ERROR;

@Log4j2
@RestControllerAdvice
public class ErrorHandlers {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseWrapper<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(ex);
        return new ResponseWrapper<>(BAD_REQUEST, ex.getBindingResult().getAllErrors().stream()
                .map(FieldError.class::cast)
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage)));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseWrapper<Map<String, String>> handleConstrainsException(ConstraintViolationException ex) {
        log.error(ex);
        Map<String, String> mapErrMess = new HashMap<>();
        int i = 1;
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            mapErrMess.put("violation_" + i, violation.getMessage());
        }
        return new ResponseWrapper<>(BAD_REQUEST, mapErrMess);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseWrapper<Object> handleGeneralException(Exception ex) {
        log.error("General Error: ", ex);
        return new ResponseWrapper<>(SYSTEM_ERROR);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseWrapper<Object> handleCommonException(CommonException e) {
        log.error("Common exception Error: ", e);
        return new ResponseWrapper<>(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseWrapper<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException Error: ", e);
        return new ResponseWrapper<>(BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseWrapper<Map<String, String>> handleIllegalArgumentException(HttpMessageNotReadableException e) {
        log.error("Http message Error: ", e);
        Map<String, String> mapErrMess = ((MismatchedInputException)e.getCause()).getPath().stream()
                .collect(Collectors.toMap(JsonMappingException.Reference::getFieldName, s -> "Không đúng định dạng"));
        return new ResponseWrapper<>(BAD_REQUEST, mapErrMess);
    }
}
