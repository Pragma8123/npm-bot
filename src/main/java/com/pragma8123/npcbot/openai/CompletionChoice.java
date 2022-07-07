package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompletionChoice {

    private String text;

    private Integer index;

    @JsonProperty("finish_reason")
    private String finishReason;
}
