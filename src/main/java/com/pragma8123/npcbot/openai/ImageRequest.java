package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
    String prompt;
    @JsonProperty("n")
    Long count;
    String size;
    @JsonProperty("response_format")
    String responseFormat;
    String user;
}
