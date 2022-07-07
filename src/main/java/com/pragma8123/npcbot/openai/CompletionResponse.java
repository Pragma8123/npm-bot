package com.pragma8123.npcbot.openai;

import lombok.Data;

import java.util.List;

@Data
public class CompletionResponse {

    private String id;

    private String object;

    private Integer created;

    private String model;

    private List<CompletionChoice> choices;
}
