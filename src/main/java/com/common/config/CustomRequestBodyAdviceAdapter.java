package com.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

import static com.common.config.LoggingConfiguration.REQUEST_ID;

@Log4j2
@RequiredArgsConstructor
@ControllerAdvice
@ConditionalOnProperty("logging.log-request")
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

  private final HttpServletRequest httpServletRequest;

  private final ObjectMapper objectMapper;

  @Override
  public boolean supports(MethodParameter methodParameter, Type type,
                          Class<? extends HttpMessageConverter<?>> aClass) {
    return true;
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                              MethodParameter parameter, Type targetType,
                              Class<? extends HttpMessageConverter<?>> converterType) {
    this.logRequest(httpServletRequest, body);

    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }

  @SneakyThrows
  private void logRequest(HttpServletRequest httpServletRequest, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    log.info(
            "BODY INCOMING REQUEST: {}: {}, body: {}, ",
            REQUEST_ID,
            requestId,
            objectMapper.writeValueAsString(body));
  }
}
