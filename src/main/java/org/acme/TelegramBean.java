package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.component.telegram.model.IncomingMessage;

@ApplicationScoped
@RegisterForReflection
public class TelegramBean {

    private static final String ID_COLUMN_NAME = "id";
    private static final String TEXT_COLUMN_NAME = "text";
    private static final String USERNAME_COLUMN_NAME = "userName";


    public String transformToJson(IncomingMessage incomingMessage) {
        return String.format(
                "{\"id\":\"%s\", \"text\":\"%s\",\"userName\":\"%s\"}",
                incomingMessage.getMessageId(),
                incomingMessage.getText(),
                incomingMessage.getFrom().getFirstName() + " " + incomingMessage.getFrom().getLastName());
    }


}
