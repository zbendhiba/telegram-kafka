package org.acme.camel.demo;

import java.util.Date;

import org.acme.camel.demo.pojo.TelegramMessage;
import org.apache.camel.component.telegram.model.IncomingMessage;

public class TelegramService {

    public TelegramMessage transform(IncomingMessage incomingMessage) {
        return new TelegramMessage()
                .withChatId(incomingMessage.getMessageId())
                .withText(incomingMessage.getText())
                .withUsername(incomingMessage.getFrom().getFirstName())
                .withDate(new Date());
    }
}
