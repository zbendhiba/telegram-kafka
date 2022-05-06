package org.acme;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.model.TelegramMessage;
import org.apache.camel.component.aws2.ddb.Ddb2Constants;
import org.apache.camel.component.telegram.model.IncomingMessage;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@ApplicationScoped
@RegisterForReflection
@Named("telegramBean")
public class TelegramBean {

    private static final String ID_COLUMN_NAME = "id";
    private static final String TEXT_COLUMN_NAME = "text";
    private static final String USERNAME_COLUMN_NAME = "userName";


    public String toKafka(IncomingMessage incomingMessage) {
        return String.format(
                "{\"id\":\"%s\", \"text\":\"%s\",\"userName\":\"%s\"}",
                incomingMessage.getMessageId(),
                incomingMessage.getText(),
                incomingMessage.getFrom().getFirstName() + " " + incomingMessage.getFrom().getLastName());

    }

    public Map toDynamo(TelegramMessage message){
        Map<String, Object> item = new HashMap<>();
        item.put(ID_COLUMN_NAME, AttributeValue.builder().s(message.getId()).build());
        item.put(TEXT_COLUMN_NAME, AttributeValue.builder().s(message.getText()).build());
        item.put(USERNAME_COLUMN_NAME, AttributeValue.builder().s(message.getUserName()).build());
        return new HashMap<>() {
            {
                put(Ddb2Constants.CONSISTENT_READ, true);
                put(Ddb2Constants.ITEM, item);
            }
        };
    }
}
