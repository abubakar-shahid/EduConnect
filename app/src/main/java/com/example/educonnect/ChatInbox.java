package com.example.educonnect;

public class ChatInbox {
    private String chatId;
    private String lastMessage;
    private long lastMessageTime;
    private String participantId; // The other user's ID
    private String participantName; // The other user's name

    public ChatInbox() {} // Required for Firebase

    public ChatInbox(String chatId, String lastMessage, long lastMessageTime, 
                    String participantId, String participantName) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.participantId = participantId;
        this.participantName = participantName;
    }

    // Getters and Setters
    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public long getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(long lastMessageTime) { this.lastMessageTime = lastMessageTime; }
    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
} 