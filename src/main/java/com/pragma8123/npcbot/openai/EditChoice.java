package com.pragma8123.npcbot.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditChoice {

    private String text;

    private Long index;
}
