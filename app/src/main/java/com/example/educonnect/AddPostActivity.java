package com.example.educonnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etSubject, etDescription, etAmount, etTokens;
    private Button btnCreatePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initViews();

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_post_title);
        etSubject = findViewById(R.id.et_post_subject);
        etDescription = findViewById(R.id.et_post_description);
        etAmount = findViewById(R.id.et_post_amount);
        etTokens = findViewById(R.id.et_post_tokens);
        btnCreatePost = findViewById(R.id.btn_create_post);
    }

    private void createPost() {
        String title = etTitle.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String tokensStr = etTokens.getText().toString().trim();

        if (title.isEmpty() || subject.isEmpty() || description.isEmpty() || amountStr.isEmpty() || tokensStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        int tokens;

        try {
            amount = Double.parseDouble(amountStr);
            tokens = Integer.parseInt(tokensStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount or tokens", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        // Create a new Post object
        Post newPost = new Post(title, subject, description, currentDateAndTime.split(" ")[0], currentDateAndTime.split(" ")[1], amount, tokens);

        // TODO: Save the new post to your data source (e.g., database, API)
        // For now, we'll just show a success message and finish the activity
        Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
