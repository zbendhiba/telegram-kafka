package org.acme;

import java.util.UUID;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;

public class Routes extends RouteBuilder {

    public void configure() throws Exception {
        Predicate isStart = jsonpath("$.text").isEqualTo("/start");

        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .bean(TelegramService.class)
                .to("direct:process-message");

        from("direct:process-message")
                .choice()
                    .when(isStart)
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
                .setHeader(AWS2S3Constants.KEY, simple(UUID.randomUUID().toString()))
                .to("aws2-s3:{{aws-s3.bucket-name}}");

        from("aws2-s3:{{aws-s3.bucket-name}}")
                .log("Incoming message from AWS3 bucket with key ${header."+AWS2S3Constants.KEY+"} : ${body}");
    }
}
