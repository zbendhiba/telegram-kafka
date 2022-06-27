package org.acme;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.model.Message;

@RegisterForReflection
public class GoogleService {

    public Message transform(List<String> googleSheetsMessage) {
        return new Message()
                .withDate(googleSheetsMessage.get(0))
                .withUsername(googleSheetsMessage.get(1))
                .withText(googleSheetsMessage.get(2));
    }
}
