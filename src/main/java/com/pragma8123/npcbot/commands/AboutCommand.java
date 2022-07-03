package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.NpcConstants;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AboutCommand implements SlashCommand {

    private final String name = "about";

    @Value("${bot.version:unknown}")
    private String botVersion;

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
                .color(NpcConstants.NPC_COLOR)
                .title("About NPC Bot")
                .addField("Version", botVersion, false)
                .build();

        return event
                .reply()
                .withEphemeral(true)
                .withEmbeds(embed);
    }
}
