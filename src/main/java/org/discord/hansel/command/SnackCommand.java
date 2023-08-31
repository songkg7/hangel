package org.discord.hansel.command;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.AllowedMentions;
import discord4j.rest.util.Color;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discord.hansel.CoupangUrlParser;
import org.discord.hansel.gcloud.SheetsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnackCommand implements SlashCommand {

    private static final String USER_ENTERED = "USER_ENTERED";

    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;
    private final SheetsService sheetsService;
    private static final String START_RANGE = "B2";

    private record SnackRequest(String name, String snack, String link) {
    }

    @Override
    public String getName() {
        return "snack";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String link = getOption(event, "link");
        try {
            String snack = CoupangUrlParser.parseQuery(link);

            User user = event.getInteraction().getUser();
            String username = user.getUsername();

            String updatedRange = updateSnack(new SnackRequest(username, snack, link));
            if (updatedRange == null) {
                return event.reply("오류가 발생했어요! 관리자에게 문의해주세요.")
                        .withEphemeral(true);
            }

            return event.reply()
                    .withEmbeds(EmbedCreateSpec.builder()
                            .addField("link", link, true)
                            .color(user.getAccentColor().orElse(Color.DISCORD_WHITE))
                            .description(snack)
                            .title("Snack Request")
                            .url(link)
                            .build()
                    )
                    .withEphemeral(false)
                    .withAllowedMentions(AllowedMentions.builder().allowUser(user.getId()).build())
                    .withContent(user.getMention() + " 님이 간식을 신청하셨어요!");
        } catch (URISyntaxException e) {
            return event.reply("올바른 쿠팡 URL이 아닌 것 같아요! 다시 확인해주세요.")
                    .withEphemeral(true);
        }
    }

    private static String getOption(ChatInputInteractionEvent event, String link) {
        return event.getOption(link)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElseThrow();
    }

    private String updateSnack(SnackCommand.SnackRequest snackRequest) {
        List<List<Object>> values = List.of(List.of(LocalDate.now().toString(), snackRequest.name(), snackRequest.snack(), snackRequest.link()));
        try {
            AppendValuesResponse response = sheetsService.getSheets().spreadsheets()
                    .values()
                    .append(spreadsheetId, START_RANGE, new ValueRange().setValues(values))
                    .setValueInputOption(USER_ENTERED)
                    .execute();
            return response.getUpdates().getUpdatedRange();
        } catch (IOException e) {
            log.error("Error while updating snack", e);
            return null;
        }
    }

}
