package com.pragma8123.npcbot.dalle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DallEConfiguration {

    @Value("${bot.dall_e.api_base_url}")
    private String dallEApiBaseUrl;

    @Bean
    public WebClient dallEWebClient() {
        return WebClient.builder()
                .baseUrl(dallEApiBaseUrl)
                .build();
    }
}
