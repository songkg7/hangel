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
        for (List<Object> row : values) {
            log.info("{}", row);
            sb.append(row.get(0)).append(" ").append(row.get(1)).append(" ").append(row.get(2)).append(" ").append(row.get(4)).append("\n");
        }
        return sb.toString();
    }
}
