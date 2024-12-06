package com.example.educonnect;

public class Post {
    private String postId;
    private String studentId;
    private String title;
    private String subject;
    private String description;
    private String date;
    private String time;
    private double amount;
    private int tokens;

    public Post(String postId, String studentId, String title, String subject, String description, String date, String time, double amount, int tokens) {
        this.postId = postId;
        this.studentId = studentId;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.tokens = tokens;
    }

    // Add getters for all fields
    public String getPostId() { return postId; }
    public String getStudentId() { return studentId; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public double getAmount() { return amount; }
    public int getTokens() { return tokens; }
}
