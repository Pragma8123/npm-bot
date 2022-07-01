package com.pragma8123.npcbot.commands;

import com.pragma8123.npcbot.openai.OpenAiService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Component
public class CompletionCommand implements SlashCommand {

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
        // Wait for a response from the OpenAI API
        event.deferReply().subscribe();

        // Get our prompt from the user. We do not need to check if this is present as it is required
        String prompt = event.getOption("prompt")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();

        // Get our prompt completion from the OpenAI API
        openAiService
                .postCompletion(prompt)
                .map(completion -> {
                    // Format our response and then update our reply
                    String response = MessageFormat.format("""
                                    ***Prompt:*** {0}
                                    {1}
                                    """, prompt, completion);

                    return event.editReply(response).subscribe();
                })
                .subscribe();

        return Mono.empty();
    }
}
