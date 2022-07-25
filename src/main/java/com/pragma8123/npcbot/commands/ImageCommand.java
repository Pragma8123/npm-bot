package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.NpcBotConstants;
import com.pragma8123.npcbot.dalle.DallEService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Component
public class ImageCommand implements SlashCommand {

    private final Logger LOG = LoggerFactory.getLogger(ImageCommand.class);

    private final String name = "image";

    private final DallEService dallEService;

    @Autowired
    public ImageCommand(DallEService dallEService) {
        this.dallEService = dallEService;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Generate an image based on your prompt using the DALL-E image engine")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("prompt")
                        .description("Prompt used to generate your image")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String prompt = event.getOption("prompt")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        return event
                .deferReply()
                .then(dallEService.generateImage(prompt))
                .flatMap(dallEResponse -> event.editReply(InteractionReplyEditSpec.builder()
                        .addFile(
                                "image.jpg",
                                new ByteArrayInputStream(Base64.getDecoder().decode(dallEResponse.getImages().get(0))))
                        .addEmbed(EmbedCreateSpec.builder()
                                .description("`" + prompt + "`")
                                .color(NpcBotConstants.NPC_COLOR)
                                .image("attachment://image.jpg")
                                .build())
                        .build()))
                .onErrorResume(error -> {
                    LOG.error(error.toString());
                    return event.editReply("There was an error generating your image!");
                })
                .then();
    }
}
