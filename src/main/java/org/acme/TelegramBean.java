package org.acme;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.model.TelegramMessage;
import org.apache.camel.component.telegram.model.IncomingMessage;

@ApplicationScoped
@RegisterForReflection
public class TelegramBean {
   public String createNotification(TelegramMessage telegramMessage){
        return String.format("You have a new incoming message %s from %s %s",
                telegramMessage.getId(),
                telegramMessage.getFirstName(),
                telegramMessage.getLastName());
    }
}
