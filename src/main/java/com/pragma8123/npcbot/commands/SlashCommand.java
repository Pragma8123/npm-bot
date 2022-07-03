package com.pragma8123.npcbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import reactor.core.publisher.Mono;

public interface SlashCommand {

    String getName();

    ImmutableApplicationCommandRequest getApplicationCommandRequest();

    Mono<Void> handle(ChatInputInteractionEvent event);

}
