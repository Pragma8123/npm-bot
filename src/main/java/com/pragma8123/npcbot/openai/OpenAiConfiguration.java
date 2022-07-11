package com.pragma8123.npcbot.openai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfiguration {

    private static final String API_BASE_URL = "https://api.openai.com/v1";

    @Bean
    public WebClient webClient() {
        return WebClient.create(API_BASE_URL);
    }
}
