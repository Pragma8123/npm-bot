package com.pragma8123.npcbot.dalle;

import io.netty.channel.epoll.EpollChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class DallEConfiguration {

    @Value("${bot.dall_e.api_base_url}")
    private String dallEApiBaseUrl;

    @Bean
    public WebClient dallEWebClient() {
        // Responses from our API can take significant amounts of time in worst case scenarios
        HttpClient client = HttpClient.create()
                .option(EpollChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 840)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 14)
                // 14 Minutes - We have to update our reply to discord within 15 minutes
                .responseTimeout(Duration.ofSeconds(840L));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(dallEApiBaseUrl)
                .build();
    }
}
