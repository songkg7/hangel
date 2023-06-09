package org.discord.hansel.command;

import com.google.api.services.sheets.v4.model.ValueRange;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.hansel.gcloud.SheetsService;
import org.discord.hansel.view.StatusView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusCommand implements SlashCommand {

    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;

    private final SheetsService sheetsService;

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        try {
            ValueRange sheet = sheetsService.getSheets().spreadsheets().values().get(spreadsheetId, "B289:F305").execute();
            return event.reply()
                    .withEphemeral(true)
                    .withContent(StatusView.from(sheet.getValues()).toString());
        } catch (IOException e) {
            log.error("Error while getting sheets", e);
            throw new RuntimeException(e);
        }
    }
}
