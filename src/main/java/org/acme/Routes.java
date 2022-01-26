package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.acme.model.TelegramMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .bean("telegramBean", "translate")
                .to("direct:process-message");

        from("direct:process-message")
                .choice()
                    .when(simple("${body} contains '/start'"))
                        .transform(simple("{{msg.bot.start}}"))
                    .when(simple("${body} contains '#msg'"))
                        .to("kafka:telegram-message")
                        .transform(simple("{{msg.bot.msg}}"))
                    .otherwise()
                        .transform(simple("{{msg.bot.otherwise}}"))
                .end()
                .setHeader(TelegramConstants.TELEGRAM_PARSE_MODE, simple(TelegramParseMode.MARKDOWN.name()))
                .to("telegram:bots?authorizationToken={{telegram-token-api}}");

        from("kafka:telegram-message")
                .log("Incoming message from Kafka topic telegram-message ${body} ")
                .unmarshal().json(TelegramMessage.class)
                .to("jpa:"+ TelegramMessage.class)
                .bean("telegramBean", "createNotification")
                .toD("slack://general?webhookUrl={{webhook-url}}");

        from("platform-http:/messages?httpMethodRestrict=GET")
                .to("jpa:"+ TelegramMessage.class+"?namedQuery=findAll")
                .marshal().json();


    }
}
