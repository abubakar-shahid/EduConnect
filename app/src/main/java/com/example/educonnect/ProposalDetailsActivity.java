package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProposalDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_details);

        // Get views
        TextView proposalText = findViewById(R.id.proposal_text_detail);
        TextView priceText = findViewById(R.id.price_detail);
        TextView timestampText = findViewById(R.id.timestamp_detail);
        Button chatButton = findViewById(R.id.start_chat_button);

        // Get data from intent
        Intent intent = getIntent();
        String tutorId = intent.getStringExtra("tutor_id");
        String message = intent.getStringExtra("message");
        String price = intent.getStringExtra("price");
        long timestamp = intent.getLongExtra("timestamp", 0);

        // Set data to views
        proposalText.setText(message);
        priceText.setText("$" + price);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(timestamp));
        timestampText.setText(date);

        // Handle chat button click
        chatButton.setOnClickListener(v -> {
            Intent chatIntent = new Intent(this, ChatActivity.class);
            chatIntent.putExtra("tutor_id", tutorId);
            startActivity(chatIntent);
        });
    }
} 