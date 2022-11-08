package com.pragma8123.npcbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelpCommand implements SlashCommand {

    private final String name = "help";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Get helpful information about the bot")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event
                .reply("Help yourself!")
                .withEphemeral(true);
    }
}
