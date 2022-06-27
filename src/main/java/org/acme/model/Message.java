package org.acme.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Message {

    String id = UUID.randomUUID().toString();
    String text;
    String userName;
    String dateTime;

    public Message() {
    }

    public Message withText(String text) {
        this.text = text;
        return this;
    }

    public Message withDate(String date) {
        this.dateTime = date;
        return this;
    }


    public Message withUsername(String name) {
        this.userName = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
