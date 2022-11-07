package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.NpcBotConstants;
import com.pragma8123.npcbot.openai.OpenAiService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionReplyEditSpec;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
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
public class HDImageCommand implements SlashCommand {

    private final Logger LOG = LoggerFactory.getLogger(HDImageCommand.class);

    private final String name = "hdimage";

    private final OpenAiService openAiService;

    @Autowired
    public HDImageCommand(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Generate an HD image based on your prompt using the OpenAI DALL-E 2.0 image engine")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("prompt")
                        .description("Prompt used to generate your image")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("size")
                        .description("The size of the generated images")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(false)
                        .addChoice(ApplicationCommandOptionChoiceData.builder()
                                .name("small")
                                .value("256x256")
                                .build())
                        .addChoice(ApplicationCommandOptionChoiceData.builder()
                                .name("medium")
                                .value("512x512")
                                .build())
                        .addChoice(ApplicationCommandOptionChoiceData.builder()
                                .name("large")
                                .value("1024x1024")
                                .build())
                        .build())
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String prompt = event.getOption("prompt")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        String size = event.getOption("size")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("MEDIUM");

        String user = event.getInteraction().getUser().getId().asString();

        return event
                .deferReply()
                .then(openAiService.getImage(prompt, 1L, size, user))
                .flatMap(imageResponse -> event.editReply(InteractionReplyEditSpec.builder()
                        .addFile(
                                "image.jpg",
                                new ByteArrayInputStream(Base64.getDecoder().decode(imageResponse.getImages().get(0).getB64Data()))
                        )
                        .addEmbed(EmbedCreateSpec.builder()
                                .description("`" + prompt + "`")
                                .color(NpcBotConstants.NPC_COLOR)
                                .image("attachment://image.jpg")
                                .build())
                        .build()))
                .onErrorResume(error -> {
                    LOG.error(error.toString());
                    return event.editReply("There was an error generating your image");
                })
                .then();
    }
}
