package com.example.educonnect;

public class Proposal {
    private String proposalId;
    private String postId;
    private String tutorId;
    private String tutorName;
    private String message;
    private String price;
    private long timestamp;

    // Required empty constructor for Firebase
    public Proposal() {}

    public Proposal(String proposalId, String postId, String tutorId, String tutorName, 
                   String message, String price, long timestamp) {
        this.proposalId = proposalId;
        this.postId = postId;
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.message = message;
        this.price = price;
        this.timestamp = timestamp;
    }

    // Add getters and setters
    public String getProposalId() { return proposalId; }
    public void setProposalId(String proposalId) { this.proposalId = proposalId; }
    
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    
    public String getTutorId() { return tutorId; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }
    
    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
} 