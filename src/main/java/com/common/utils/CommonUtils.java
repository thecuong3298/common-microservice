package com.common.utils;

import com.common.dto.ResponseWrapper;
import com.common.rest.error.CommonException;
import lombok.experimental.UtilityClass;

import static com.common.rest.response.CommonErrorCode.SUCCESS;

@UtilityClass
public class CommonUtils {
  public <T> void checkResponseSuccess(ResponseWrapper<T> responseWrapper) {
    if (!SUCCESS.getCode().equals(responseWrapper.getCode())) {
        throw new CommonException(responseWrapper.getCode(), responseWrapper.getMessage());
    }
  }
}
