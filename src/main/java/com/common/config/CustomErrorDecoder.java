package com.common.config;

import com.common.rest.error.CommonException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = errorDecoder.decode(methodKey, response);
        log.error("Feign exception: status: {}, message: {}", response.status(), exception.getMessage(), exception);
        switch (response.status()) {
            case 400:
                return new CommonException("400", exception.getMessage() != null ? exception.getMessage() : "Bad Request");
            case 404:
                return new CommonException("404", exception.getMessage() != null ? exception.getMessage() : "Not found");
            default:
               return exception;
        }
    }
}
