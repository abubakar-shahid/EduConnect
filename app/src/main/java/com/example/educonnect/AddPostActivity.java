package com.example.educonnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etSubject, etDescription, etAmount, etTokens;
    private Button btnCreatePost;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();

        btnCreatePost.setOnClickListener(v -> createPost());
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

        if (title.isEmpty() || subject.isEmpty() || description.isEmpty() || 
            amountStr.isEmpty() || tokensStr.isEmpty()) {
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

        // Disable button while processing
        btnCreatePost.setEnabled(false);

        // Create post data
        Map<String, Object> post = new HashMap<>();
        post.put("title", title);
        post.put("subject", subject);
        post.put("description", description);
        post.put("amount", amount);
        post.put("tokens", tokens);
        post.put("studentId", mAuth.getCurrentUser().getUid());
        post.put("createdAt", new Date());
        post.put("status", "active");

        // Save to Firestore
        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddPostActivity.this, 
                            "Post created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddPostActivity.this, 
                            "Error creating post: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    btnCreatePost.setEnabled(true);
                });
    }
}
