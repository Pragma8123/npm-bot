package com.pragma8123.npcbot.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAiService {

    private static final String API_BASE_URL = "https://api.openai.com/v1";

    private static final Integer MAX_TOKENS = 1024;

    private static final Integer TEMPERATURE = 1;

    private final WebClient webClient;

    @Value("${bot.openai.org_id}")
    private String openAiOrgId;

    @Value("${bot.openai.api_key}")
    private String openAiApiKey;

    @Value("${bot.openai.model}")
    private String openAiModel;

    public OpenAiService() {
        this.webClient = WebClient.create(API_BASE_URL);
    }

    public OpenAiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CompletionResponse> getCompletion(String prompt) {

        CompletionRequest body = new CompletionRequest();

        body.setModel(openAiModel);
        body.setPrompt(prompt);
        body.setMaxTokens(MAX_TOKENS);
        body.setTemperature(TEMPERATURE);

        return this.webClient.post()
                .uri("/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("OpenAI-Organization", openAiOrgId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(CompletionResponse.class);
    }

}
