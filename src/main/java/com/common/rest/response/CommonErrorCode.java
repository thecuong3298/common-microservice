package com.common.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCodeResponse {

    SUCCESS("200", "Thành Công"),
    TIMEOUT("555", "Timeout"),
    SYSTEM_ERROR("9999", "Lỗi hệ thống");
    private final String code;
    private final String message;
}
