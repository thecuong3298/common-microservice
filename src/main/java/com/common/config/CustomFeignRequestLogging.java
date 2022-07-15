package com.common.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.common.config.LoggingConfiguration.REQUEST_ID;
import static feign.Logger.Level.HEADERS;

@Log4j2
public class CustomFeignRequestLogging extends Logger {

  @Override
  protected void logRequest(String configKey, Level logLevel, Request request) {

    if (logLevel.ordinal() >= HEADERS.ordinal()) {
      super.logRequest(configKey, logLevel, request);
    } else {
      String body = null;
      byte[] bytes = request.body();
      if (bytes != null) {
        body = new String(bytes, StandardCharsets.UTF_8);
      }
      log.info(
          "FEIGN REQUEST ---> {}: {}, method: {}, url: {}, header: {}, body: {}",
          REQUEST_ID,
          request.headers().get(REQUEST_ID).stream().findAny().orElse(null),
          request.httpMethod().name(),
          request.url(),
          request.headers(),
          body);
    }
  }

  @Override
  protected Response logAndRebufferResponse(
      String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
    if (logLevel.ordinal() >= HEADERS.ordinal()) {
      super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
    } else {
      int status = response.status();
      Request request = response.request();
      log.info(
          "FEIGN RESPONSE ---> {}: {}, method: {}, url: {}, status: {}, body: {}, elapsedTime: {}ms",
          REQUEST_ID,
          request.headers().get(REQUEST_ID).stream().findAny().orElse(null),
          request.httpMethod().name(),
          request.url(),
          status,
          StreamUtils.copyToString(response.body().asInputStream(), Charset.defaultCharset()),
          elapsedTime);
    }
    return response;
  }

  @Override
  protected void log(String configKey, String format, Object... args) {
    log.info(format(configKey, format, args));
  }

  protected String format(String configKey, String format, Object... args) {
    return String.format(methodTag(configKey) + format, args);
  }
}
