package com.common.dto;

import com.common.rest.response.CommonErrorCode;
import com.common.rest.response.ErrorCodeResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapper<T> {

    private String code;

    private String message;

    private T data;

    public ResponseWrapper(T data) {
        this.data = data;
        this.code = CommonErrorCode.SUCCESS.getCode();
        this.message = CommonErrorCode.SUCCESS.getMessage();
    }

    public ResponseWrapper(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseWrapper(ErrorCodeResponse errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    public ResponseWrapper(ErrorCodeResponse errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
