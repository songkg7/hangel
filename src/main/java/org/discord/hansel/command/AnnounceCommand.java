package org.discord.hansel.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.util.AllowedMentions;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AnnounceCommand implements SlashCommand {
    @Override
    public String getName() {
        return "announce";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply()
                .withAllowedMentions(AllowedMentions.builder().parseType(AllowedMentions.Type.EVERYONE_AND_HERE).build())
                .withContent("@everyone\n간식 신청 공지 테스트");
    }
}
