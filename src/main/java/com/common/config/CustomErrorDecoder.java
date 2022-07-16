package com.common.config;

import com.common.rest.error.CommonException;
import com.common.rest.response.CommonErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import static com.common.rest.response.CommonErrorCode.*;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = errorDecoder.decode(methodKey, response);
        log.error("Feign exception: status: {}, message: {}", response.status(), exception.getMessage(), exception);
        switch (response.status()) {
            case 400:
                return new CommonException(BAD_REQUEST, exception.getMessage() != null ? exception.getMessage() : "Bad Request");
            case 401:
                return new CommonException(INVALID_TOKEN, exception.getMessage() != null ? exception.getMessage() : "Unauthorized");
            case 403:
                return new CommonException(FORBIDDEN, exception.getMessage() != null ? exception.getMessage() : "Forbidden");
            case 404:
                return new CommonException(NOT_FOUND, exception.getMessage() != null ? exception.getMessage() : "Not found");
            case 504:
                return new CommonException(TIMEOUT, exception.getMessage() != null ? exception.getMessage() : "Gateway Timeout");
            default:
               return exception;
        }
    }
}
