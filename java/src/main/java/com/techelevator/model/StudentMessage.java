package com.techelevator.model;

public class StudentMessage {

    private int id;
    private String body;
    private String sender;

    public StudentMessage(int id, String body, String sender) {
        this.id = id;
        this.body = body;
        this.sender = sender;
    }

    public StudentMessage(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
