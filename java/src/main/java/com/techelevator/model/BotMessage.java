package com.techelevator.model;

public class BotMessage extends Message {

    private int responseId;
    private int questionId;
    private String codeeStyle;

    public BotMessage(){}

    private String sender = "bot";

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getCodeeStyle() {
        return codeeStyle;
    }

    public void setCodeeStyle(String codeeStyle) {
        this.codeeStyle = codeeStyle;
    }
}

