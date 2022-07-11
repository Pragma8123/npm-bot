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
import java.util.Optional;

@Component
public class CompletionCommand implements SlashCommand {

    private final Logger LOG = LoggerFactory.getLogger(CompletionCommand.class);

    private final String name = "completion";

    @Autowired
    private OpenAiService openAiService;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImmutableApplicationCommandRequest getApplicationCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(name)
                .description("Give NPC a prompt to complete")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("prompt")
                        .description("The prompt to be completed")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        // Get our prompt from the user
        String prompt = event.getOption("prompt")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        // Get tts option or default to false
        Boolean tts = event.getOption("tts")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asBoolean)
                .orElse(false);

        return event
                .deferReply()
                .then(openAiService.getCompletion(prompt)
                        .flatMap(completionResponse -> {

                            // We want our first completion choice from our response
                            String completion = completionResponse.getChoices().get(0).getText();
                            String response = MessageFormat.format("""
                                    ***Prompt:*** {0}
                                    {1}
                                    """, prompt, completion);

                            return event.editReply(response);
                        })
                        .onErrorResume(error -> {
                            LOG.error(error.toString());
                            return event.editReply("There was an error completing your prompt!");
                        }))
                .then();
    }
}
