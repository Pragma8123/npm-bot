package com.pragma8123.npcbot.dalle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DallEService {

    private final WebClient webClient;

    @Autowired
    public DallEService(WebClient dallEWebClient) {
        this.webClient = dallEWebClient;
    }

    public Mono<DallEResponse> generateImage(String prompt) {
        return webClient.post()
                .uri("/dalle")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new DallERequest(prompt, 1L))
                .retrieve()
                .bodyToMono(DallEResponse.class);
    }
}
