package com.pragma8123.npcbot.dalle;

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

public class DallEServiceTest {

    private static MockWebServer mockBackEnd;

    private DallEService dallEService;

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

        dallEService = new DallEService(webClient);
    }

    @Test
    void ensureResponseFromGenerateImage() throws Exception {
        DallEResponse mockDallEResponse = new DallEResponse(
                Collections.singletonList("This is a test."),
                "jpeg"
        );

        mockBackEnd.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(new ObjectMapper().writeValueAsString(mockDallEResponse)));

        Mono<DallEResponse> generateImageResponseMono = dallEService.generateImage("This is a test!");
        StepVerifier.create(generateImageResponseMono)
                .expectNextMatches(generateImageResponse -> generateImageResponse.equals(mockDallEResponse))
                .verifyComplete();
    }
}
