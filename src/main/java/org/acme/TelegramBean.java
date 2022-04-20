package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.component.telegram.model.IncomingMessage;

@ApplicationScoped
@RegisterForReflection
@Named("telegramBean")
public class TelegramBean {

    public String translate(IncomingMessage incomingMessage) {
        return String.format(
                "{\"id\":%s, \"text\":\"%s\",\"userName\":\"%s\"}",
                incomingMessage.getMessageId(),
                incomingMessage.getText(),
                incomingMessage.getFrom().getFirstName() + " " + incomingMessage.getFrom().getLastName());

    }

    public String generateNotification(TelegramMessage telegramMessage) {
        return String.format("%s: %s t'envoie un nouveau message: %s" , telegramMessage.getId(), telegramMessage.getUserName(), telegramMessage.getText());
    }
}
