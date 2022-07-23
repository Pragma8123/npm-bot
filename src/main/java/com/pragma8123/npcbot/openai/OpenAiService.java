package com.pragma8123.npcbot.openai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAiService {

    private final WebClient webClient;

    @Value("${bot.openai.completion_model}")
    private String openAiCompletionModel;

    @Value("${bot.openai.edit_model}")
    private String openAiEditModel;

    @Value("${bot.openai.temperature}")
    private Long temperature;

    @Autowired
    public OpenAiService(WebClient openAiWebClient) {
        this.webClient = openAiWebClient;
    }

    public Mono<CompletionResponse> getCompletion(String prompt, Long maxTokens) {

        CompletionRequest body = new CompletionRequest();

        body.setModel(openAiCompletionModel);
        body.setPrompt(prompt);
        body.setMaxTokens(maxTokens);
        body.setTemperature(temperature);

        return webClient.post()
                .uri("/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(CompletionResponse.class);
    }

    public Mono<EditResponse> getEdit(String input, String instructions) {

        EditRequest body = new EditRequest();

        body.setModel(openAiEditModel);
        body.setInput(input);
        body.setInstruction(instructions);

        return webClient.post()
                .uri("/edits")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(EditResponse.class);
    }
}
