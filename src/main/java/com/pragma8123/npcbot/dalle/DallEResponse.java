package com.pragma8123.npcbot.dalle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
public class DallEResponse {

    private List<byte[]> images;

    @JsonProperty("generatedImgsFormat")
    private String imageFormat;

    // Use custom setter, our images are returned as Base64-encoded strings
    @JsonSetter("generatedImgs")
    public void setImages(List<String> encodedImages) {
        images = new ArrayList<>();
        for (String image : encodedImages) {
            images.add(Base64.getDecoder().decode(image));
        }
    }
}
