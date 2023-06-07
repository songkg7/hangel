package org.discord.hansel.gcloud;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@Service
public class SheetsService {

    private static final String APPLICATION_NAME = "Hansel Discord Bot for Snack Request";

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

}
