package com.example.educonnect;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatMessageAdapter messageAdapter;
    private List<ChatMessage> messageList;
    private EditText messageInput;
    private Button sendButton;
    private FirebaseDatabase database;
    private String chatId;
    private String currentUserId;
    private String otherUserId;
    private String otherUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        // Get data from intent
        otherUserId = getIntent().getStringExtra("other_user_id");
        otherUserName = getIntent().getStringExtra("other_user_name");
        
        // Generate unique chat ID (smaller ID first to maintain consistency)
        chatId = generateChatId(currentUserId, otherUserId);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_chat);
        messageInput = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);

        // Setup RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(messageList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Load existing messages
        loadMessages();

        // Send message handler
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        database.getReference("chats").child(chatId).child("messages")
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
                    if (message != null) {
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
                @Override
                public void onChildRemoved(DataSnapshot snapshot) {}
                @Override
                public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) return;

        // Generate message ID
        String messageId = database.getReference().push().getKey();
        long timestamp = System.currentTimeMillis();

        // Create message object
        ChatMessage message = new ChatMessage(messageId, currentUserId, messageText, timestamp);

        // Save message
        database.getReference("chats").child(chatId).child("messages").child(messageId)
            .setValue(message)
            .addOnSuccessListener(aVoid -> {
                messageInput.setText("");
                updateChatInbox(messageText, timestamp);
            })
            .addOnFailureListener(e -> 
                Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());
    }

    private void updateChatInbox(String lastMessage, long timestamp) {
        // Update current user's inbox
        ChatInbox currentUserInbox = new ChatInbox(chatId, lastMessage, timestamp, 
            otherUserId, otherUserName);
        database.getReference("chat_inbox").child(currentUserId).child(chatId)
            .setValue(currentUserInbox);

        // Get current user's name and update other user's inbox
        FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        database.getReference("users").child(currentUserId).child("fullName")
            .get().addOnSuccessListener(snapshot -> {
                String currentUserName = snapshot.getValue(String.class);
                ChatInbox otherUserInbox = new ChatInbox(chatId, lastMessage, timestamp, 
                    currentUserId, currentUserName);
                database.getReference("chat_inbox").child(otherUserId).child(chatId)
                    .setValue(otherUserInbox);
            });
    }

    private String generateChatId(String uid1, String uid2) {
        // Ensure consistent chat ID regardless of who initiates
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }
}
