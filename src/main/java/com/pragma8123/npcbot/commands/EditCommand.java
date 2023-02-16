package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.modal.EditModal;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EditCommand implements SlashCommand {

    private final String name = "edit";

    private final EditModal editModal;

    public EditCommand(EditModal editModal) {
        this.editModal = editModal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Edit your text based on provided instructions")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.presentModal(editModal.getModalSpec());
    }
}
