package com.pragma8123.npcbot.modal;

import com.pragma8123.npcbot.openai.OpenAiService;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Component
public class EditModal implements Modal {

    private final String id = "edit-modal";

    private final String inputTextInputId = "edit-input";

    private final String instructionsTextInputId = "instructions-input";

    private final OpenAiService openAiService;

    @Autowired
    public EditModal(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public InteractionPresentModalSpec getModalSpec() {
        return InteractionPresentModalSpec.builder()
                .title("Text Edit")
                .customId(id)
                .addComponent(ActionRow.of(TextInput
                        .paragraph(inputTextInputId, "Input")))
                .addComponent(ActionRow.of(TextInput
                        .small(instructionsTextInputId, "Instructions", "Fix my spelling errors.")))
                .build();
    }

    @Override
    public Mono<Void> handle(ModalSubmitInteractionEvent event) {
        String input = "";
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (inputTextInputId.equals(component.getCustomId())) {
                input = component.getValue().orElse("");
            }
        }

        String instructions = "";
        for (TextInput component : event.getComponents(TextInput.class)) {
            if (instructionsTextInputId.equals(component.getCustomId())) {
                instructions = component.getValue().orElse("");
            }
        }

        String finalInput = input;
        String finalInstructions = instructions;
        return event
                .deferReply()
                .then(openAiService.getEdit(input, instructions))
                .flatMap(editResponse -> event
                        .editReply(MessageFormat.format("""
                                        **Input:**
                                        {0}
                                        **Instructions:**
                                        {1}
                                        **Edited:**
                                        {2}
                                        """,
                                finalInput,
                                finalInstructions,
                                editResponse.getChoices().get(0).getText())))
                .onErrorResume(error -> event
                        .editReply("There was an error editing your text!"))
                .then();
    }
}
