package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    Long created;
    @JsonProperty("data")
    List<GeneratedImage> images;
}
