package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenAiServiceTest {

    private static MockWebServer mockBackEnd;

    private OpenAiService openAiService;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        WebClient webClient = WebClient.create(baseUrl);

        openAiService = new OpenAiService(webClient);
    }

    @Test
    void ensureResponseFromGetCompletion() throws Exception {
        CompletionChoice completionChoice = new CompletionChoice();
        completionChoice.setText("This is a response from a test!");
        completionChoice.setIndex(0);
        completionChoice.setFinishReason("testing");

        List<CompletionChoice> choices = new ArrayList<>();
        choices.add(completionChoice);

        TokenUsage tokenUsage = new TokenUsage();
        tokenUsage.setPromptTokens(0L);
        tokenUsage.setCompletionTokens(0L);
        tokenUsage.setTotalTokens(0L);

        CompletionResponse mockCompletionResponse = new CompletionResponse();
        mockCompletionResponse.setId("test-id");
        mockCompletionResponse.setModel("test-model");
        mockCompletionResponse.setObject("test-object");
        mockCompletionResponse.setCreated(12345L);
        mockCompletionResponse.setChoices(choices);
        mockCompletionResponse.setUsage(tokenUsage);

        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(mockCompletionResponse)));

        Mono<CompletionResponse> completionResponseMono = openAiService.getCompletion("This is a test!");
        StepVerifier.create(completionResponseMono)
                .expectNextMatches(completionResponse -> completionResponse.equals(mockCompletionResponse))
                .verifyComplete();
    }

    @Test
    void ensureResponseFromGetEdit() throws Exception {
        EditChoice mockEditChoice = new EditChoice();
        mockEditChoice.setIndex(0L);
        mockEditChoice.setText("This is a mock response.");

        List<EditChoice> mockEditChoices = new ArrayList<>();
        mockEditChoices.add(mockEditChoice);

        TokenUsage tokenUsage = new TokenUsage();
        tokenUsage.setPromptTokens(0L);
        tokenUsage.setCompletionTokens(0L);
        tokenUsage.setTotalTokens(0L);

        EditResponse mockEditResponse = new EditResponse();
        mockEditResponse.setObject("test-object");
        mockEditResponse.setCreated(12345L);
        mockEditResponse.setChoices(mockEditChoices);
        mockEditResponse.setUsage(tokenUsage);

        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(mockEditResponse)));

        Mono<EditResponse> editResponseMono = openAiService.getEdit("This is a test!", "Don't fail!");
        StepVerifier.create(editResponseMono)
                .expectNextMatches(editResponse -> editResponse.equals(mockEditResponse))
                .verifyComplete();
    }
}
