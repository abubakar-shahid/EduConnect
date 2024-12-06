package com.example.educonnect;

public class Proposal {
    private String proposalId;  // Unique ID for the proposal
    private String postId;      // ID of the post this proposal is for
    private String studentId;   // ID of the student who posted
    private String tutorId;     // ID of the tutor who submitted proposal
    private String tutorName;   // Name of the tutor
    private String proposal;    // Proposal text
    private double amount;      // Proposed amount
    private long timestamp;     // When the proposal was submitted

    // Required empty constructor for Firebase
    public Proposal() {}

    public Proposal(String proposalId, String postId, String studentId, String tutorId, 
            String tutorName, String proposal, double amount, long timestamp) {
        this.proposalId = proposalId;
        this.postId = postId;
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.proposal = proposal;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getProposalId() { return proposalId; }
    public void setProposalId(String proposalId) { this.proposalId = proposalId; }
    
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
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