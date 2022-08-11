package com.techelevator.model;

public class MotivationalQuote extends Message{

    public MotivationalQuote(){}

    private String q;
    private String a;
    private String h;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }
}

