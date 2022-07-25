package com.pragma8123.npcbot.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditResponse {

    private String object;

    private Long created;

    private List<EditChoice> choices;

    private TokenUsage usage;
}
