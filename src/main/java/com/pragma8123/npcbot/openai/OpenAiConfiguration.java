package com.pragma8123.npcbot.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfiguration {

    @Value("${bot.openai.api_base_url}")
    private String openAiApiBaseUrl;

    @Value("${bot.openai.org_id}")
    private String openAiOrgId;

    @Value("${bot.openai.api_key}")
    private String openAiApiKey;

    @Bean
    public WebClient openAiWebClient() {
        return WebClient.builder()
                .baseUrl(openAiApiBaseUrl)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("OpenAI-Organization", openAiOrgId)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(8_389_000))
                        .build())
                .build();
    }
}
