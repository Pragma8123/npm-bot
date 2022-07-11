package com.pragma8123.npcbot.openai;

import lombok.Data;

@Data
public abstract class OpenAiResponse {

    private String object;

    private Long created;

    private TokenUsage usage;
}
