package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletionRequest {

    private String model;

    private String prompt;

    @JsonProperty("max_tokens")
    private Number maxTokens;

    private Number temperature;
}
