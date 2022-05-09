package org.acme.model;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class TelegramMessage {

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Long chatId;
    String text;
    String userName;
    String dateTime;

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

    public TelegramMessage withDate(Date date){
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
