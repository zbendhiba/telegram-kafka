package org.acme;

import org.acme.model.TelegramMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;

public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .transform(simple("{\"message\":\"${body}\"}"))
                .to("kafka:telegram-message")
                .transform(constant("{{msg.bot.response}}"))
                .to("telegram:bots?authorizationToken={{telegram-token-api}}");

        from("kafka:telegram-message")
                .log("Incoming message from Kafka topic telegram-message ${body} ")
                .unmarshal().json(TelegramMessage.class)
                .to("jpa:"+ TelegramMessage.class);

        from("platform-http:/messages?httpMethodRestrict=GET")
                .to("jpa:"+ TelegramMessage.class+"?namedQuery=findAll")
                .marshal().json();


    }
}
