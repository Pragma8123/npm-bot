package com.pragma8123.npcbot;

import com.pragma8123.npcbot.commands.SlashCommand;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GlobalCommandRegistrar implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLineRunner.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private List<? extends SlashCommand> commands;

    @Override
    public void run(String... args) {

        List<ApplicationCommandRequest> applicationCommandRequests = new ArrayList<>();
        commands.forEach((command) -> {
            applicationCommandRequests.add(command.getApplicationCommandRequest());
        });

        // Convenience variables for the sake of easier to read code below.
        final ApplicationService applicationService = restClient.getApplicationService();
        final long applicationId = restClient.getApplicationId().block();

        /*
        Bulk overwrite commands. This is now idempotent, so it is safe to use this even when only 1 command
        is changed/added/removed
        */
        applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, applicationCommandRequests)
                .doOnNext(ignore -> LOG.debug("Successfully registered Global Commands"))
                .doOnError(e -> LOG.error("Failed to register global commands", e))
                .subscribe();
    }
}