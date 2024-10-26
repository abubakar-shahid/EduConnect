package com.example.educonnect;

public class Post {
    private String title;
    private String subject;
    private String description;
    private String date;
    private String time;
    private double amount;

    public Post(String title, String subject, String description, String date, String time, double amount) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    // Add getters for all fields
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public double getAmount() { return amount; }
}
