package org.acme;

import org.acme.model.TelegramMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.ddb.Ddb2Operations;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;

public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .bean("telegramBean", "toKafka")
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
                .bean("telegramBean", "toDynamo")
                .log("transformed for Dynamo ${body}")
                .to("aws2-ddb://telegram?operation="+ Ddb2Operations.PutItem)
            ;

      /*  from("platform-http:/messages?httpMethodRestrict=GET")
                .to("jpa:"+ TelegramMessage.class+"?namedQuery=findAll")
                .marshal().json();*/


    }
}
