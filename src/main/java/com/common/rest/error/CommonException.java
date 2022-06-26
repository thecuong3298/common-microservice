package com.common.rest.error;

import com.common.dto.ResponseWrapper;
import com.common.rest.response.ErrorCodeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private String code;

    private String message;

    private Object data;

    public CommonException(ErrorCodeResponse errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public CommonException(ErrorCodeResponse errorCode, Object data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    public CommonException(ResponseWrapper<Object> restResponse) {
        this.code = restResponse.getCode();
        this.message = restResponse.getMessage();
    }

    public CommonException(CommonException errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
    }

    public CommonException(CommonException errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}



