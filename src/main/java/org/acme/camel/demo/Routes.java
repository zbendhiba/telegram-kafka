package org.acme.camel.demo;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Routes extends RouteBuilder {

    @Override
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


        from("aws2-s3:{{aws-s3.bucket-name}}")
                .log("${body}")
                .unmarshal().json(JsonLibrary.Jackson, java.util.Map.class)
                .log("unmarshalled: ${body}")
                .process(exchange -> {
                    final Message m = exchange.getMessage();
                    Map<String, Object> body = m.getBody(Map.class);
                    m.setHeader("CamelGoogleSheets.valueInputOption", "RAW");
                    m.setHeader("CamelGoogleSheets.values",
                            new ValueRange().setValues(
                                    Arrays.asList(
                                            Arrays.asList(
                                                    body.get("dateTime"),
                                                    body.get("userName"),
                                                    body.get("text")
                                            ))));

                })
                // Throttle to avoid exceeding the default Google API limit of 60 requests per min
                .throttle(1).timePeriodMillis(1500)
                .to("google-sheets://data/append?spreadsheetId={{google-sheets.spreadsheet-id}}&range=Sheet1!A1:A3");
    }
}
