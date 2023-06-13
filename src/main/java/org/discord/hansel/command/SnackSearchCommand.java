package org.discord.hansel.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionReplyEditMono;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SnackSearchCommand implements SlashCommand {
    @Override
    public String getName() {
        return "snack-search";
    }

    // 일반적인 응답은 3초 안에 결과를 받아야 Discord 에서 에러를 주지 않는다. == 3초 rule
    // https://www.coupang.com/np/search?component=&q=꼬북칩&channel=user
    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        // 1. 간식 이름 입력
        // 2. 쿠팡에서 간식 이름으로 검색
        // 3. 상위 3개 결과 크롤링
        // 4. 유저가 결과 중 하나를 선택할 때까지 대기
        // 5. 선택한 결과를 Embed 로 보여줌
        String snackName = event.getOption("snack-name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElseThrow();// required option 이라서 반드시 값이 존재함
        log.info("snackName: {}", snackName);

        // http request to coupang
        // html parser... or api...
        InteractionReplyEditMono reply = event.editReply().withEmbeds(
                EmbedCreateSpec.builder()
                        .title("간식 검색 결과")
                        .description(snackName)
                        .build()
        );

        return event.deferReply()
                .withEphemeral(true)
                .then(reply)
                .then();
    }
}
