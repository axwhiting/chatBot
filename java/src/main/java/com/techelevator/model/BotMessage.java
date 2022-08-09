package com.techelevator.model;

public class BotMessage extends Message {

    public BotMessage(){}

    private void setSenderToBot() {
        this.setSender("bot");
    }

}

