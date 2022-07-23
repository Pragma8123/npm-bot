package com.pragma8123.npcbot.dalle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DallERequest {

    private String text;

    @JsonProperty("num_images")
    private Long numImages;
}
