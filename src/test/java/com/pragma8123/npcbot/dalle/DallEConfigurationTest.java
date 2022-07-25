package com.pragma8123.npcbot.dalle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

public class DallEConfigurationTest {

    ApplicationContextRunner context;

    @BeforeEach
    public void beforeEach() {
        context = new ApplicationContextRunner()
                .withUserConfiguration(DallEConfiguration.class);
    }

    @Test
    public void ensureWebClientBean() {
        context.run(it -> assertThat(it).hasSingleBean(WebClient.class));
    }
}
