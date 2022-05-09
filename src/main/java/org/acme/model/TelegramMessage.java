package org.acme.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class TelegramMessage {

    Long chatId;
    String text;
    String userName;
    String dateTime;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public TelegramMessage() {
    }

    public TelegramMessage withChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public TelegramMessage withText(String text) {
        this.text = text;
        return this;
    }

    public TelegramMessage withDate(Date date) {
        this.dateTime = DATE_FORMAT.format(date);
        return this;
    }


    public TelegramMessage withUsername(String firstName, String lastName) {
        this.userName = firstName + " " + lastName;
        return this;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
