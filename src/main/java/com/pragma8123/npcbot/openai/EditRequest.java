package com.pragma8123.npcbot.openai;

import lombok.Data;

@Data
public class EditRequest {

    private String model;

    private String input;

    private String instruction;
}
