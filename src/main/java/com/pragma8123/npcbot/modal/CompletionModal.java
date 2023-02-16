package com.pragma8123.npcbot.modal;

import com.pragma8123.npcbot.openai.OpenAiService;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Component
public class CompletionModal implements Modal {

    private final String id = "completion-modal";

    private final String completionTextInputId = "completion-prompt";
    private final OpenAiService openAiService;

    @Value("${bot.openai.max_tokens}")
    private Long maxTokens;

    public CompletionModal(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public InteractionPresentModalSpec getModalSpec() {
        return InteractionPresentModalSpec.builder()
                .title("Text Completion")
                .customId(id)
                .addComponent(ActionRow.of(TextInput.paragraph(completionTextInputId, "Prompt").required(true)))
                .build();
    }

    @Override
    public Mono<Void> handle(ModalSubmitInteractionEvent event) {
        String prompt = "";
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (completionTextInputId.equals(component.getCustomId())) {
                prompt = component.getValue().orElse("");
            }
        }

        String finalPrompt = prompt;
        return event
                .deferReply()
                .then(openAiService.getCompletion(prompt, maxTokens))
                .flatMap(completionResponse -> event
                        .editReply(MessageFormat.format("""
                                        {0}
                                        {1}
                                        """,
                                finalPrompt,
                                completionResponse.getChoices().get(0).getText())))
                .onErrorResume(error -> event
                        .editReply("There was an error completing your prompt!"))
                .then();
    }
}
