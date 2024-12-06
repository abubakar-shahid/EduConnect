package com.example.educonnect;

public class Proposal {
    private String tutorId;
    private String tutorName;
    private String proposal;
    private double amount;
    private long timestamp;

    // Required empty constructor for Firebase
    public Proposal() {}

    public Proposal(String tutorId, String tutorName, String proposal, double amount, long timestamp) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.proposal = proposal;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Add getters and setters
    public String getTutorId() { return tutorId; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }
    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }
    public String getProposal() { return proposal; }
    public void setProposal(String proposal) { this.proposal = proposal; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
} 