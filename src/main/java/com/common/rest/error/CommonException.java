package com.common.rest.error;

import com.common.dto.ResponseWrapper;
import com.common.rest.response.ErrorCodeResponse;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;


    public CommonException(ErrorCodeResponse errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(ErrorCodeResponse errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(ResponseWrapper<Object> restResponse) {
        this.code = restResponse.getCode();
        this.message = restResponse.getMessage();
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(CommonException errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(CommonException errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(String code, String message) {
        this.code = code;
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }
}



