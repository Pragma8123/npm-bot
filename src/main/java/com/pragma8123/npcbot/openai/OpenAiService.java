package com.pragma8123.npcbot.openai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAiService {

    private static final Integer MAX_TOKENS = 1024;

    private static final Integer TEMPERATURE = 1;

    private final WebClient webClient;

    @Value("${bot.openai.completion_model}")
    private String openAiCompletionModel;

    @Value("${bot.openai.edit_model}")
    private String openAiEditModel;

    @Autowired
    public OpenAiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CompletionResponse> getCompletion(String prompt) {

        CompletionRequest body = new CompletionRequest();

        body.setModel(openAiCompletionModel);
        body.setPrompt(prompt);
        body.setMaxTokens(MAX_TOKENS);
        body.setTemperature(TEMPERATURE);

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
