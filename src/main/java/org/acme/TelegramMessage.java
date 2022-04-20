package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;


public class TelegramMessage {
    Long id;
    String text;
    String userName;

    public TelegramMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
