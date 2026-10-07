package com.barcode.recopo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient aiServerWebClient(
            @Value("${ai.server.url:http://localhost:8000}") String aiServerUrl
    ) {
        return WebClient.builder()
                .baseUrl(aiServerUrl)
                .build();
    }
}