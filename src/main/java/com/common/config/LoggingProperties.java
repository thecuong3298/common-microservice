package com.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "logging")
public class LoggingProperties {

    private Boolean logRequest = false;

    private Boolean logResponse = false;

    private Logstash logstash;

    @Data
    public static class Logstash {
        /**
         * enable logstash
         * default false
         */
        private Boolean enable = false;

        /**
         * host logstash
         * default localhost
         */
        private String host = "localhost";

        /**
         * logstash port
         * default 5000
         */
        private Integer port = 5000;
    }
}
