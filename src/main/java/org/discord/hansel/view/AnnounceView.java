package org.discord.hansel.view;

public final class AnnounceView {

    private AnnounceView() {
    }

    public static String create() {
        StringBuilder sb = new StringBuilder();
        sb.append("@everyone 간식 신청 공지");
        sb.append("\n");
        sb.append("스프레드시트를 사용하여 신청하거나, /snack 명령어를 사용해서 신청해주세요!");
        return sb.toString();
    }

}
