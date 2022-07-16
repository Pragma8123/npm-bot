package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.modal.CompletionModal;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CompletionCommand implements SlashCommand {

    private final String name = "completion";

    private final CompletionModal completionModal;

    @Autowired
    public CompletionCommand(CompletionModal completionModal) {
        this.completionModal = completionModal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Give NPC a prompt to complete")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.presentModal(completionModal.getModalSpec());
    }
}
