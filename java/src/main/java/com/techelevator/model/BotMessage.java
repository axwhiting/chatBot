package com.techelevator.model;

public class BotMessage {

    private String body;

    public BotMessage(String body) {
        this.body = body;
    }

    public BotMessage(){}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
