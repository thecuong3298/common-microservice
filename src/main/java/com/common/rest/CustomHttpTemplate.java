package com.common.rest;

import com.common.rest.error.CommonException;
import com.common.rest.error.TimeoutException;
import com.common.rest.response.CommonErrorCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.SocketTimeoutException;
import java.util.Map;

public abstract class CustomHttpTemplate {


    protected abstract RestTemplate getRestTemplate();

    protected <R> ResponseEntity<R> callHttp(String url, HttpMethod method, Object requestBody,
                                             Class<R> clazz, Map<String, ?> paramMap, HttpHeaders headers) {
        try {
            HttpEntity<?> httpEntity = new HttpEntity<>(requestBody, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            for (Map.Entry<String, ?> entry : paramMap.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
            return this.getRestTemplate()
                    .exchange(builder.toUriString(), method, httpEntity, clazz);
        } catch (ResourceAccessException resourceAccessException) {
            if (resourceAccessException.getCause() instanceof SocketTimeoutException) {
                throw new TimeoutException();
            }
            throw new CommonException(CommonErrorCode.SYSTEM_ERROR);
        }
    }
}
