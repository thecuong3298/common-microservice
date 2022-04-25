package com.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(
        prefix = "spring.rest-template",
        ignoreUnknownFields = false
)
public class RestConfigProperties {

    /**
     * client use in common request authentication
     */
    private String basicClient;

    /**
     * client secret use in common request authentication
     */
    private String basicSecret;

    /**
     * connect timeout (second unit)  default 15
     */
    private Long connectTimeout = 15L;

    /**
     * read timeout (second unit) default 15
     */
    private Long readTimeout = 15L;
}
