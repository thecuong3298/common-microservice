package com.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.layout.JsonLayout;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfiguration {
    public static final String REQUEST_ID = "request_id";

    public LoggingConfiguration(LoggingProperties logging) {
        LoggingProperties.Logstash logstash = logging.getLogstash();
        if (Boolean.FALSE.equals(logstash.getEnable())) {
            return;
        }
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.getRootLogger().addAppender(SocketAppender.newBuilder()
                .setName("socket")
                .setHost(logstash.getHost())
                .setPort(logstash.getPort())
                .setReconnectDelayMillis(5000)
                .setLayout(JsonLayout.newBuilder().setProperties(true).build())
                .build());
        ctx.updateLoggers();
    }

    @Bean
    @ConditionalOnProperty("logging.log-request")
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

    @Bean
    @ConditionalOnProperty("logging.log-request")
    public FilterRegistrationBean<CustomURLFilter> filterRegistrationBean() {
        FilterRegistrationBean<CustomURLFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomURLFilter());
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
