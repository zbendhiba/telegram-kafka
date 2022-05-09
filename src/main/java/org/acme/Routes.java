package org.acme;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;

public class Routes extends RouteBuilder {

    public void configure() throws Exception {

        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .bean(TelegramService.class)
                .to("direct:process-message");

        from("direct:process-message")
                .choice()
                    .when(simple("${body} contains '/start'"))
                        .transform(simple("{{msg.bot.start}}"))
                    .otherwise()
                        .marshal().json()
                        .to("kafka:telegram-message")
                        .transform(simple("{{msg.bot.msg}}"))
                .end()
                .setHeader(TelegramConstants.TELEGRAM_PARSE_MODE, simple(TelegramParseMode.MARKDOWN.name()))
                .to("telegram:bots?authorizationToken={{telegram-token-api}}");

        from("kafka:telegram-message")
                .log("Incoming message from Kafka topic telegram-message ${body}")
        ;

    }
}
