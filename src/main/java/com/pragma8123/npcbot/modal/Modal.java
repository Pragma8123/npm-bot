package com.pragma8123.npcbot.modal;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.spec.InteractionPresentModalSpec;
import reactor.core.publisher.Mono;

public interface Modal {

    String getId();

    InteractionPresentModalSpec getModalSpec();

    Mono<Void> handle(ModalSubmitInteractionEvent event);
}
