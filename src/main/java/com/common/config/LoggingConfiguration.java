package com.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.layout.JsonLayout;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

    public LoggingConfiguration(LogstashProperties logstash) {
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
}
