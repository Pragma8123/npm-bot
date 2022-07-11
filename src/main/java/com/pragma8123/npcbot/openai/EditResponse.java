package com.pragma8123.npcbot.openai;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EditResponse extends OpenAiResponse {

    private List<EditChoice> choices;
}
