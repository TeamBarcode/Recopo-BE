package com.barcode.recopo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient aiServerWebClient() {
        return WebClient.builder()
                .baseUrl("https://quantum-suction-catnap.ngrok-free.dev")
                .build();
    }
}