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
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logRequest((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Nothing here
    }

    private void logRequest(HttpServletRequest request) {
        if (request != null) {
            String requestId = request.getHeader(REQUEST_ID);
            if (Strings.isBlank(requestId)) {
                requestId = UUID.randomUUID().toString();
            }
            request.setAttribute(REQUEST_ID, requestId);
            log.info(
                    "INCOMING REQUEST: {}: {}, method: {}, url: {}, queries: {}",
                    REQUEST_ID,
                    requestId,
                    request.getMethod(),
                    replaceUntrustedRequestData(request.getRequestURI()),
                    replaceUntrustedRequestData(request.getQueryString()));
        }
    }

    private String replaceUntrustedRequestData(String asRequestData) {
        if (Strings.isNotBlank(asRequestData)) {
            return asRequestData.replaceAll("[\n\r\t]", "_");
        }
        return asRequestData;
    }
}
