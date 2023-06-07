package org.discord.hansel.command;

import com.google.api.services.sheets.v4.model.ValueRange;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.hansel.gcloud.SheetsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

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
            List<List<Object>> values = sheet.getValues();
            StringBuilder sb = new StringBuilder();
            sb.append("간식 신청 목록").append("\n");
            for (List<Object> row : values) {
                log.info("{}", row);
                sb.append(row.get(0)).append(" ").append(row.get(1)).append(" ").append(row.get(2)).append(" ").append(row.get(4)).append("\n");
            }

            return event.reply()
                    .withEphemeral(true)
                    .withContent(sb.toString());
        } catch (IOException e) {
            log.error("Error while getting sheets", e);
            throw new RuntimeException(e);
        }
    }
}
