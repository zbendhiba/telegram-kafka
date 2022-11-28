package org.acme.camel.demo;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.acme.camel.demo.pojo.TelegramMessage;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Routes extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

        Predicate isStart = jsonpath("$.text").isEqualTo("/start");

        from(telegram("bots"))
                .choice()
                    .when(isStart)
                        .transform(constant("{{msg.bot.start}}"))
                    .otherwise()
                        .bean(TelegramService.class)
                        .marshal().json()
                        .to(kafka("telegram-message"))
                        .transform(constant("{{msg.bot.msg}}"))
                    .end()
                .to("telegram:bots");

        from(kafka("telegram-message"))
                .log("Incoming message from Kafka topic telegram-message ${body}")
                .setHeader(AWS2S3Constants.KEY, () -> UUID.randomUUID().toString())
                .log(String.format("Sending message with header :: ${header.%s}", AWS2S3Constants.KEY))
                .to(aws2S3("{{aws-s3.bucket-name}}"));


        from(aws2S3("{{aws-s3.bucket-name}}")
                    .delay(1500))
                .log("${body}")
                .unmarshal().json(TelegramMessage.class)
                .process(exchange -> {
                    final Message m = exchange.getMessage();
                    TelegramMessage body = m.getBody(TelegramMessage.class);
                    m.setHeader("CamelGoogleSheets.valueInputOption", "RAW");
                    m.setHeader("CamelGoogleSheets.values",
                            new ValueRange().setValues(
                                    Arrays.asList(
                                            Arrays.asList(
                                                    body.getDateTime(),
                                                    body.getUserName(),
                                                    body.getText()
                                            ))));

                })
                // Throttle to avoid exceeding the default Google API limit of 60 requests per min
                .throttle(1).timePeriodMillis(1500)
                .to("google-sheets://data/append?spreadsheetId={{google-sheets.spreadsheet-id}}&range=Sheet1!A1:A3");
    }
}
