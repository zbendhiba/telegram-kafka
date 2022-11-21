package org.acme.camel.demo;

import java.util.Arrays;
import java.util.Map;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("aws2-s3:{{aws-s3.bucket-name}}?delay=1500")
              //  .log("${body}")
                .to("kafka:talk");

        from("kafka:talk")
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
                                                    body.get("title"),
                                                    body.get("track")
                                            ))));

                })
                // Throttle to avoid exceeding the default Google API limit of 60 requests per min
                .throttle(1).timePeriodMillis(1500)
                .to("google-sheets://data/append?spreadsheetId={{google-sheets.spreadsheet-id}}&range=Sheet1!A1:A3");
    }
}
