package com.pragma8123.npcbot.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletionResponse {

    private String id;

    private String object;

    private Long created;

    private String model;

    private List<CompletionChoice> choices;

    private TokenUsage usage;
}
