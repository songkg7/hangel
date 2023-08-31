package org.discord.hansel;

import java.net.URI;
import java.net.URISyntaxException;

public final class CoupangUrlParser {

    private CoupangUrlParser() {
    }

    public static String parseQuery(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String query = uri.getQuery();

        String[] split = query.split("&");
        for (String s : split) {
            if (s.startsWith("q=")) {
                return s.split("=")[1];
            }
        }
        return "";
    }

}
