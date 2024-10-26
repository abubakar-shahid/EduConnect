package com.example.educonnect;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        TextView titleTextView = findViewById(R.id.post_title);
        TextView subjectTextView = findViewById(R.id.post_subject);
        TextView descriptionTextView = findViewById(R.id.post_description);
        TextView dateTimeTextView = findViewById(R.id.post_date_time);
        TextView amountTextView = findViewById(R.id.post_amount);
        TextView tokensTextView = findViewById(R.id.post_tokens);
        Button submitProposalButton = findViewById(R.id.submit_proposal_button);

        Post post = (Post) getIntent().getSerializableExtra("post");

        if (post != null) {
            titleTextView.setText(post.getTitle());
            subjectTextView.setText(post.getSubject());
            descriptionTextView.setText(post.getDescription());
            dateTimeTextView.setText(post.getDate() + " " + post.getTime());
            amountTextView.setText("$" + post.getAmount());
            tokensTextView.setText(post.getTokens() + " tokens required");

            submitProposalButton.setOnClickListener(v -> {
                // TODO: Implement proposal submission logic
                // You can reuse the showProposalDialog() method from PostAdapter here
            });
        }
    }
}
