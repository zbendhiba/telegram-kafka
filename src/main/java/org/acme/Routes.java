package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.model.TelegramMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;

@ApplicationScoped
public class Routes extends RouteBuilder {
    @Inject
    TelegramBean myBean;

    @Override
    public void configure() throws Exception {
        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .bean(myBean, "translate")
                .to("direct:process-message");

        from("direct:process-message")
                .choice()
                    .when(simple("${body} contains '/start'"))
                        .transform(simple("{{msg.bot.start}}"))
                    .when(simple("${body} contains '#msg'"))
                        .transform(simple("{{msg.bot.msg}}"))
                    .otherwise()
                        .transform(simple("{{msg.bot.otherwise}}"))
                .end()
                .setHeader(TelegramConstants.TELEGRAM_PARSE_MODE, simple(TelegramParseMode.MARKDOWN.name()))
                .to("telegram:bots?authorizationToken={{telegram-token-api}}");


    }
}
