package com.example.bit371finaltodolist;

public class Item {
    private int id;
    private String title;
    private String date;
    private boolean done;

    public Item(int id, String title, String date, boolean done) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.done = done;
    }

    public String getTitle() { return title; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public int getId() { return id; }

    public boolean isDone() { return done; }
}
