package org.acme.camel.demo.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TelegramMessage {
    Long telegramId;
    String text;
    String userName;
    String dateTime;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public TelegramMessage() {
    }

    public TelegramMessage withChatId(Long telegramId) {
        this.telegramId = telegramId;
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


    public TelegramMessage withUsername(String name) {
        this.userName = name;
        return this;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
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
