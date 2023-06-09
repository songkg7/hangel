package org.discord.hansel.gcloud;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.discord.hansel.command.SnackCommand;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class SheetsService {

    private static final String APPLICATION_NAME = "Hansel Discord Bot for Snack Request";
    private static final String START_RANGE = "B2";

    public Sheets getSheets() {
        try {
            return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), GoogleAuthorizeUtil.authorize())
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error while getting sheets", e);
            throw new RuntimeException(e);
        }
    }

    public void updateSnack(String spreadsheetId, SnackCommand.SnackRequest snackRequest) throws IOException {
        List<List<Object>> values = List.of(List.of(LocalDate.now().toString(), snackRequest.name(), snackRequest.snack(), snackRequest.link()));
        getSheets().spreadsheets()
                .values()
                .append(spreadsheetId, START_RANGE, new ValueRange().setValues(values))
                .setValueInputOption("USER_ENTERED")
                .execute();
    }
}
