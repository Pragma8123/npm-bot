package com.pragma8123.npcbot.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditRequest {

    private String model;

    private String input;

    private String instruction;
}
