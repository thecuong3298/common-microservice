package com.common.config;

import com.common.rest.LoggingRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RestConfig {

    private final RestConfigProperties restConfigProperties;

    @Bean
    @Primary
    public RestTemplate restTemplate(
            RestTemplateBuilder restTemplateBuilder) {
        // fix 401 response throw HttpRetryException
        restTemplateBuilder
                .requestFactory(() -> {
                    final CloseableHttpClient httpClient = HttpClientBuilder
                            .create()
                            .disableRedirectHandling()
                            .build();
                    return new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
                })
                .interceptors(new LoggingRequestInterceptor())
                .defaultHeader(HttpHeaders.ACCEPT, "*/*")
                .setConnectTimeout(Duration.ofSeconds(restConfigProperties.getConnectTimeout()))
                .setReadTimeout(Duration.ofSeconds(restConfigProperties.getReadTimeout()));
        if (Strings.isNotBlank(restConfigProperties.getBasicClient())) {
            restTemplateBuilder.basicAuthentication(restConfigProperties.getBasicClient(), restConfigProperties.getBasicSecret());
        }
        return restTemplateBuilder.build();
    }
}
