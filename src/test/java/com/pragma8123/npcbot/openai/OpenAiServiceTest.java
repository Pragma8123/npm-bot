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
import java.util.Collections;

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
        CompletionResponse mockCompletionResponse = new CompletionResponse(
                "test-id",
                "test-object",
                12345L,
                "test-model",
                Collections.singletonList(
                        new CompletionChoice("This is a test.", 0, "testing")),
                new TokenUsage(0L, 0L, 0L)
        );

        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(new ObjectMapper().writeValueAsString(mockCompletionResponse)));

        Mono<CompletionResponse> completionResponseMono = openAiService.getCompletion("This is a test!", 1024L);
        StepVerifier.create(completionResponseMono)
                .expectNextMatches(completionResponse -> completionResponse.equals(mockCompletionResponse))
                .verifyComplete();
    }

    @Test
    void ensureResponseFromGetEdit() throws Exception {
        EditResponse mockEditResponse = new EditResponse(
                "test-object",
                12345L,
                Collections.singletonList(new EditChoice("This is a test.", 0L)),
                new TokenUsage(0L, 0L, 0L)
        );

        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(new ObjectMapper().writeValueAsString(mockEditResponse)));

        Mono<EditResponse> editResponseMono = openAiService.getEdit("This is a test!", "Don't fail!");
        StepVerifier.create(editResponseMono)
                .expectNextMatches(editResponse -> editResponse.equals(mockEditResponse))
                .verifyComplete();
    }
}
