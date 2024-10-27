package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatMessageAdapter messageAdapter;
    private List<ChatMessage> messageList;
    private EditText editTextChatbox;
    private Button buttonChatboxSend;
    private String chatPartnerName;
    private boolean isStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatPartnerName = getIntent().getStringExtra("chat_partner_name");
        isStudent = getIntent().getBooleanExtra("is_student", true);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(chatPartnerName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_chat);
        editTextChatbox = findViewById(R.id.edittext_chatbox);
        buttonChatboxSend = findViewById(R.id.button_chatbox_send);

        messageList = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        loadMessages();

        buttonChatboxSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextChatbox.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    ChatMessage message = new ChatMessage(messageText, true); // true for sent by user
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    editTextChatbox.setText("");
                    // TODO: Send message to server
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (isStudent) {
            intent = new Intent(this, StudentDashboardActivity.class);
        } else {
            intent = new Intent(this, TutorDashboardActivity.class);
        }
        intent.putExtra("open_chats_tab", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void loadMessages() {
        // TODO: Load actual messages from a database or API
        // For now, we'll add some dummy messages
        messageList.add(new ChatMessage("Hello!", false));
        messageList.add(new ChatMessage("Hi there!", true));
        messageList.add(new ChatMessage("How can I help you today?", false));
        messageAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}
