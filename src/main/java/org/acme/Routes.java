package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class Routes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("telegram:bots")
                .bean("telegramBean", "translate")
                .to("kafka:telegram-topic")
                .transform(constant("Merci pour votre message!"))
                .to("telegram:bots");

        from("kafka:telegram-topic")
                .log("incoming message ${body}")
                .unmarshal().json(TelegramMessage.class)
                .bean("telegramBean",  "generateNotification")
                .to("slack:#myslack");
    }
}
