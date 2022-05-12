package org.acme;

import java.util.UUID;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;

public class Routes extends RouteBuilder {

    public void configure() throws Exception {
        Predicate isStart = jsonpath("$.text").isEqualTo("/start");

        from("telegram:bots")
                .choice()
                    .when(isStart)
                        .transform(simple("{{msg.bot.start}}"))
                    .otherwise()
                        .bean(TelegramService.class)
                        .marshal().json()
                        .to("kafka:telegram-message")
                        .transform(simple("{{msg.bot.msg}}"))
                .end()
                .to("telegram:bots");

        from("kafka:telegram-message")
                .log("Incoming message from Kafka topic telegram-message ${body}")
                .setHeader(AWS2S3Constants.KEY, simple(UUID.randomUUID().toString()))
                .log(String.format("Sending message with header :: ${header.%s}", AWS2S3Constants.KEY))
                .to("aws2-s3:{{aws-s3.bucket-name}}");
    }

}
