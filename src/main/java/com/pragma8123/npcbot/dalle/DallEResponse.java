package com.pragma8123.npcbot.dalle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DallEResponse {

    @JsonProperty("generatedImgs")
    private List<String> images;

    @JsonProperty("generatedImgsFormat")
    private String imageFormat;
}
