package com.common.rest.error;

import com.common.rest.response.RestResponse;
import com.common.rest.response.ErrorCodeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private HttpStatus httpStatus;

    private String code;

    private String message;

    public CommonException(ErrorCodeResponse errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(RestResponse restResponse) {
        this.code = restResponse.getCode();
        this.message = restResponse.getMessage();
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(CommonException errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
        this.httpStatus = HttpStatus.OK;
    }

    public CommonException(HttpStatus httpStatus, CommonException errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = httpStatus;
    }

    public CommonException(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value() + "";
        this.message = message;
        this.httpStatus = httpStatus;
    }

}



