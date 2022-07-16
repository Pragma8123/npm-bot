package com.pragma8123.npcbot.listeners;


import com.pragma8123.npcbot.modal.Modal;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Component
public class ModalSubmitListener {

    private final Collection<Modal> modals;

    @Autowired
    public ModalSubmitListener(List<Modal> modals, GatewayDiscordClient client) {
        this.modals = modals;
        client.on(ModalSubmitInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(ModalSubmitInteractionEvent event) {
        return Flux.fromIterable(modals)
                .filter(modal -> modal.getId().equals(event.getCustomId()))
                .next()
                .flatMap(modal -> modal.handle(event));
    }
}
