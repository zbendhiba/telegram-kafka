package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class Routes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("telegram:bots")
                .bean("telegramBean", "translate")
                .to("kafka:toto")
                .transform(constant("Merci pour votre message!"))
                .to("telegram:bots");

        from("kafka:toto")
                .log("Incoming from kafka ${body}")
                .unmarshal().json(TelegramMessage.class)
                .bean("telegramBean", "generateNotification")
                .log("${body}")
                .to("slack:#myslack");

    }
}
