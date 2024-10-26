package com.example.educonnect;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // Get post details from intent
        String title = getIntent().getStringExtra("title");
        String subject = getIntent().getStringExtra("subject");
        String description = getIntent().getStringExtra("description");
        String dateTime = getIntent().getStringExtra("dateTime");
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        int tokens = getIntent().getIntExtra("tokens", 0);

        // Set post details to views
        TextView titleView = findViewById(R.id.post_title);
        TextView subjectView = findViewById(R.id.post_subject);
        TextView descriptionView = findViewById(R.id.post_description);
        TextView dateTimeView = findViewById(R.id.post_date_time);
        TextView amountView = findViewById(R.id.post_amount);
        TextView tokensView = findViewById(R.id.post_tokens);
        Button submitProposalButton = findViewById(R.id.submit_proposal_button);

        titleView.setText(title);
        subjectView.setText(subject);
        descriptionView.setText(description);
        dateTimeView.setText(dateTime);
        amountView.setText("$" + amount);
        tokensView.setText(tokens + " tokens required");

        submitProposalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProposalDialog();
            }
        });
    }

    private void showProposalDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_proposal_dialog);

        // Set dialog to appear at the center of the screen
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextInputEditText proposalInput = dialog.findViewById(R.id.proposal_input);
        TextInputEditText amountInput = dialog.findViewById(R.id.amount_input);
        Button submitButton = dialog.findViewById(R.id.submit_proposal_dialog_button);

        submitButton.setOnClickListener(v -> {
            String proposal = proposalInput.getText().toString();
            String amount = amountInput.getText().toString();

            if (proposal.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Implement proposal submission logic
                Toast.makeText(this, "Proposal submitted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
