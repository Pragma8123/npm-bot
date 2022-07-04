package com.pragma8123.npcbot.openai;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompletionRequest {

    private String model;
    private String prompt;
    @JsonProperty("max_tokens")
    private Number maxTokens;
    private Number temperature;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Number getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Number getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
}
