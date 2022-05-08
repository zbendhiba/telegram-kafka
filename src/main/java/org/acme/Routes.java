package org.acme;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.acme.model.TelegramMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.ddb.Ddb2Constants;
import org.apache.camel.component.aws2.ddb.Ddb2Operations;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;
import org.apache.camel.component.telegram.model.IncomingMessage;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class Routes extends RouteBuilder {
    private static final String ID_COLUMN_NAME = "id";
    private static final String TEXT_COLUMN_NAME = "text";
    private static final String USERNAME_COLUMN_NAME = "userName";

    @Override
    public void configure() throws Exception {

        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .bean(TelegramBean.class)
                .to("direct:process-message");

        from("direct:process-message")
                .choice()
                    .when(simple("${body} contains '/start'"))
                        .transform(simple("{{msg.bot.start}}"))
                    .otherwise()
                        .to("kafka:telegram-message")
                        .transform(simple("{{msg.bot.otherwise}}"))
                .end()
                .setHeader(TelegramConstants.TELEGRAM_PARSE_MODE, simple(TelegramParseMode.MARKDOWN.name()))
                .to("telegram:bots?authorizationToken={{telegram-token-api}}");

        from("kafka:telegram-message")
                .log("Incoming message from Kafka topic telegram-message ${body} ")
                .unmarshal().json(TelegramMessage.class)
                .process(e-> {
                    TelegramMessage message = e.getMessage().getBody(TelegramMessage.class);
                    Map<String, Object> item = createItem(message, ID_COLUMN_NAME, TEXT_COLUMN_NAME, USERNAME_COLUMN_NAME);
                    e.getMessage().setHeader(Ddb2Constants.CONSISTENT_READ, true);
                    e.getMessage().setHeader(Ddb2Constants.ITEM, item);
                })
                .log("transformed for Dynamo ${body}")
                .to("aws2-ddb://telegram?operation="+ Ddb2Operations.PutItem)
            ;

    }

    static Map<String, Object> createItem(TelegramMessage message, String idColumnName, String textColumnName, String usernameColumnName) {
        Map<String, Object> item = new HashMap<>();
        item.put(idColumnName, AttributeValue.builder().s(message.getId()).build());
        item.put(textColumnName, AttributeValue.builder().s(message.getText()).build());
        item.put(usernameColumnName, AttributeValue.builder().s(message.getUserName()).build());
        return item;
    }
}
