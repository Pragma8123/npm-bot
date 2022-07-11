package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.openai.OpenAiService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Component
public class EditCommand implements SlashCommand {

    private final Logger LOG = LoggerFactory.getLogger(EditCommand.class);

    private final String name = "edit";

    private final OpenAiService openAiService;

    @Autowired
    public EditCommand(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public String getName() {
        return name;
    }

    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Edit your text based on provided instructions")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("input")
                        .description("Your text to be edited")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("instructions")
                        .description("Instructions to edit prompt with")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {

        // Get our input from the user
        String input = event.getOption("input")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        // Get our edit instructions from the user
        String instructions = event.getOption("instructions")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        return event
                .deferReply()
                .then(openAiService.getEdit(input, instructions))
                .map(editResponse -> {
                    String editedText = editResponse.getChoices().get(0).getText();
                    return MessageFormat.format("""
                            **Original:** {0}
                            **Instructions:** {1}
                            **Edited Text:** {2}
                            """, input, instructions, editedText);
                })
                .flatMap(event::editReply)
                .onErrorResume(error -> {
                    LOG.error(error.toString());
                    return event.editReply("There was an error editing your text!");
                })
                .then();
    }
}
