package com.pragma8123.npcbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class AboutCommand implements SlashCommand {

    @Value("${bot.version:unknown}")
    private String botVersion;

    private final String name = "about";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Returns information about NPC bot")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.BLUE)
                .title("About NPC Bot")
                .addField("Version", botVersion, false)
                .timestamp(Instant.now())
                .build();

        return event
                .reply()
                .withEphemeral(true)
                .withEmbeds(embed);
    }
}
