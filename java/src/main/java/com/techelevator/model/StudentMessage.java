package com.techelevator.model;

public class StudentMessage extends Message{

    public StudentMessage(){}

    private void setSenderToStudent() {
        this.setSender("student");
    }
}
