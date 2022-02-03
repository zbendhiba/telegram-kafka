package org.acme;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.component.telegram.model.IncomingMessage;

@ApplicationScoped
@RegisterForReflection
public class TelegramBean {

    public String translate(IncomingMessage incomingMessage) {
        return String.format(
                "{\"id\":%s, \"text\":\"%s\",\"userName\":\"%s\"}",
                incomingMessage.getMessageId(),
                incomingMessage.getText(),
                incomingMessage.getFrom().getFirstName() + " " + incomingMessage.getFrom().getLastName());
    }

}
