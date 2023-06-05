package org.discord.hansel.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelpCommand implements SlashCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        ActionRow actionRow = ActionRow.of(
                Button.primary("testButton", "테스트 버튼"),
                Button.danger("testButton2", "테스트 버튼2"),
                Button.link("https://www.google.com", "구글로 이동")
        );
        return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .addComponent(actionRow)
                .build());
    }
}
