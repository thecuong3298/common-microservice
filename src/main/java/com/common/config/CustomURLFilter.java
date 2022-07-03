package com.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

import static com.common.config.LoggingConfiguration.REQUEST_ID;


@Slf4j
public class CustomURLFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) {
	// Nothing here
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    String requestId = UUID.randomUUID().toString();
    servletRequest.setAttribute(REQUEST_ID, requestId);
    logRequest((HttpServletRequest) servletRequest, requestId);
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    // Nothing here
  }

  private void logRequest(HttpServletRequest request, String requestId) {
    if (request != null) {
      StringBuilder data = new StringBuilder();
      data.append("\nSTART REQUEST------------------------------\n")
          .append("[REQUEST-ID]: ").append(requestId).append("\n")
          .append("[PATH]: ").append(replaceUntrustedRequestData(request.getRequestURI())).append("\n")
          .append("[QUERIES]: ").append(replaceUntrustedRequestData(request.getQueryString())).append("\n");
      log.info("[DATA]:{}", data);
    }
  }

  private String replaceUntrustedRequestData(String asRequestData) {
    if (Strings.isNotBlank(asRequestData)) {
      return asRequestData.replaceAll("[\n\r\t]", "_");
    }
    return asRequestData;
  }
}
