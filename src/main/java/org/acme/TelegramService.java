package org.acme;

import java.util.Date;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.model.TelegramMessage;
import org.apache.camel.component.telegram.model.IncomingMessage;

@RegisterForReflection
public class TelegramService {

    public TelegramMessage transform(IncomingMessage incomingMessage) {
        return new TelegramMessage()
                .withChatId(incomingMessage.getMessageId())
                .withText(incomingMessage.getText())
                .withUsername(incomingMessage.getFrom().getFirstName())
                .withDate(new Date());
    }
}
