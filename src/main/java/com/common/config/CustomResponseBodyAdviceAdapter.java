package com.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.common.config.LoggingConfiguration.REQUEST_ID;

@Log4j2
@RequiredArgsConstructor
@ControllerAdvice
@ConditionalOnProperty("logging.log-response")
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

  private final ObjectMapper objectMapper;

  @Override
  public boolean supports(MethodParameter methodParameter,
                          Class<? extends HttpMessageConverter<?>> aClass) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object o,
                                MethodParameter methodParameter,
                                MediaType mediaType,
                                Class<? extends HttpMessageConverter<?>> aClass,
                                ServerHttpRequest serverHttpRequest,
                                ServerHttpResponse serverHttpResponse) {

    if (serverHttpRequest instanceof ServletServerHttpRequest &&
      serverHttpResponse instanceof ServletServerHttpResponse) {
      this.logResponse(((ServletServerHttpRequest) serverHttpRequest).getServletRequest(), o);
    }

    return o;
  }

  @SneakyThrows
  private void logResponse(HttpServletRequest httpServletRequest, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    StringBuilder data = new StringBuilder();
    data.append("\nLOGGING RESPONSE START-----------------------------------\n")
      .append("[REQUEST-ID]: ").append(requestId).append("\n")
      .append("[BODY RESPONSE]: ").append("\n\n")
      .append(objectMapper.writeValueAsString(body))
      .append("\n\n")
      .append("LOGGING RESPONSE END-----------------------------------\n");
    log.info(data.toString());
  }
}
