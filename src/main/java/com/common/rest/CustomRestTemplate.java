package com.common.rest;

import com.common.dto.ResponseWrapper;
import com.common.rest.error.CommonException;
import com.common.rest.error.TimeoutException;
import com.common.rest.response.CommonErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Log4j2
@Getter
@Component
public class CustomRestTemplate extends CustomHttpTemplate {

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Autowired
    public CustomRestTemplate(ObjectMapper objectMapper,
                              RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * @param url         request url
     * @param method      http method
     * @param requestBody request body
     * @param responseType       type of request body object
     * @param paramMap    query parameter map
     * @param headers     request headers
     * @param <R>         type of response
     * @return response object
     */
    @NonNull
    @SneakyThrows
    private <R> ResponseEntity<R> callHttpInternal(String url, HttpMethod method,
                                                   Object requestBody, ParameterizedTypeReference<R> responseType,
                                                   Map<String, ?> paramMap, HttpHeaders headers) {
        try {
            log.debug("Rest {} request to {}:\n {}", method.name(), url, requestBody);
            ResponseEntity<R> response = this.callHttp(url, method, requestBody, responseType, paramMap, headers);
            log.debug("Rest {} response from {}:\n {}", method.name(), url, response);
            return response;
        } catch (RestClientResponseException restClientResponseException) {
            log.error("Rest client error: ", restClientResponseException);
            if (restClientResponseException.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                throw new CommonException(CommonErrorCode.BAD_REQUEST, restClientResponseException.getMessage());
            } else if (restClientResponseException.getRawStatusCode() == HttpStatus.FORBIDDEN.value()) {
                throw new CommonException(CommonErrorCode.FORBIDDEN);
            } else if (restClientResponseException.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
                throw new CommonException(CommonErrorCode.FORBIDDEN);
            } else if (restClientResponseException.getRawStatusCode() == HttpStatus.GATEWAY_TIMEOUT.value()) {
                throw new CommonException(CommonErrorCode.TIMEOUT);
            }
        } catch (TimeoutException e) {
            log.error(e.getMessage(), e);
            throw new CommonException(CommonErrorCode.TIMEOUT);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new CommonException(CommonErrorCode.SYSTEM_ERROR);
    }

    /**
     * @param url      request url
     * @param responseType    type of request body object
     * @param paramMap query parameter map
     * @param <R>      type of response
     * @return response object
     */
    public <R> R get(String url, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap) {
        return this.callHttpInternal(url, HttpMethod.GET, null, responseType, paramMap, new HttpHeaders()).getBody();
    }

    /**
     * @param url      request url
     * @param responseType    type of request body object
     * @param paramMap query parameter map
     * @param <R>      type of response
     * @return response entity object
     */
    public <R> ResponseEntity<R> getResponseEntity(String url, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap, HttpHeaders headers) {
        return this.callHttpInternal(url, HttpMethod.GET, null, responseType, paramMap, headers);
    }

    /**
     * @param url         request url
     * @param requestBody request body object
     * @param responseType       type of request body object
     * @param paramMap    query parameter map
     * @param <R>         type of response
     * @return response object
     */
    public <R> R post(String url, Object requestBody, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap, HttpHeaders headers) {
        return this
                .callHttpInternal(url, HttpMethod.POST, requestBody, responseType, paramMap,
                        headers).getBody();
    }

    /**
     * @param url         request url
     * @param requestBody request body object
     * @param responseType       type of request body object
     * @param paramMap    query parameter map
     * @param <R>         type of response
     * @return response object
     */
    public <R> R put(String url, Object requestBody, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap, HttpHeaders headers) {
        return this.callHttpInternal(url, HttpMethod.PUT, requestBody, responseType, paramMap, headers).getBody();
    }

    /**
     * @param url         request url
     * @param requestBody request body object
     * @param responseType       type of request body object
     * @param paramMap    query parameter map
     * @param <R>         type of response
     * @return response object
     */
    public <R> R patch(String url, Object requestBody, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap) {
        return this
                .callHttpInternal(url, HttpMethod.PATCH, requestBody, responseType, paramMap,
                        new HttpHeaders()).getBody();
    }

    /**
     * @param url      request url
     * @param responseType    type of request body object
     * @param paramMap query parameter map
     * @param <R>      type of response
     * @return response object
     */
    public <R> R delete(String url, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap) {
        return this.callHttpInternal(url, HttpMethod.DELETE, null, responseType, paramMap, new HttpHeaders()).getBody();
    }

    /**
     * @param url      request url
     * @param responseType    type of request body object
     * @param paramMap query parameter map
     * @param <R>      type of response
     * @return response object
     */
    public <R> R trace(String url, ParameterizedTypeReference<R> responseType, Map<String, ?> paramMap) {
        return this
                .callHttpInternal(url, HttpMethod.TRACE, null, responseType, paramMap, new HttpHeaders()).getBody();
    }


}
