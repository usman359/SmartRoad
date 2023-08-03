package com.ass2.smart_road;

public class myModel {
    String text, date_and_time;

    public myModel(String text, String date_and_time) {
        this.text = text;
        this.date_and_time = date_and_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }
}
