package org.discord.hansel.view;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public final class StatusView {

    private final List<List<Object>> values;

    private StatusView(List<List<Object>> values) {
        this.values = values;
    }

    public static StatusView from(List<List<Object>> values) {
        return new StatusView(values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("간식 신청 목록").append("\n");
        List<List<Object>> refinedValues = values.stream()
                .filter(row -> row.size() == 5)
                .toList();
        for (List<Object> row : refinedValues) {
            Object date = row.get(1);
            Object name = row.get(2);
            Object snackName = row.get(3);
            sb.append(date).append(" ").append(name).append(" ").append(snackName).append("\n");
        }
        return sb.toString();
    }
}
