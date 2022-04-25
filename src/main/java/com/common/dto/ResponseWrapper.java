package com.common.dto;

import com.common.rest.response.CommonErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ResponseWrapper<T> {

    private String code;

    private String message;

    private T data;

    public ResponseWrapper(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseWrapper(T data) {
        this.data = data;
        this.code = CommonErrorCode.SUCCESS.getCode();
        this.message = CommonErrorCode.SUCCESS.getMessage();
    }

    public ResponseWrapper(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
