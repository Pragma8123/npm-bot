package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenUsage {

    @JsonProperty("prompt_tokens")
    private Long promptTokens;

    @JsonProperty("completion_tokens")
    private Long completionTokens;

    @JsonProperty("total_tokens")
    private Long totalTokens;
}
