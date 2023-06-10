package org.discord.hansel.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.hansel.gcloud.SheetsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnackCommand implements SlashCommand {

    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;

    private final SheetsService sheetsService;

    public record SnackRequest(String name, String snack, String link) {}

    @Override
    public String getName() {
        return "snack";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String snack = getOption(event, "snack");
        String link = getOption(event, "link");
        User user = event.getInteraction().getUser();
        String username = user.getUsername();

        try {
            sheetsService.updateSnack(spreadsheetId, new SnackRequest(username, snack, link));
        } catch (IOException e) {
            log.error("Error while updating sheets", e);
            throw new RuntimeException(e);
        }

        return event.reply()
                .withEmbeds(EmbedCreateSpec.builder()
                        .addField("link", link, true)
                        .color(Color.WHITE)
                        .description(snack)
                        .url(link)
                        .build()
                )
                .withEphemeral(false)
                .withContent("간식 신청 완료!");
    }

    private static String getOption(ChatInputInteractionEvent event, String link) {
        return event.getOption(link)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElseThrow();
    }
}
