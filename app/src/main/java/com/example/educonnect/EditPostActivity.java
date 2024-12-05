package com.example.educonnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {

    private static final String TAG = "EditPostActivity";
    private TextInputEditText etTitle, etSubject, etDescription, etAmount, etTokens;
    private Button btnEditSave;
    private boolean isEditing = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        loadPostData();

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    savePost();
                } else {
                    enableEditing();
                }
            }
        });
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_post_title);
        etSubject = findViewById(R.id.et_post_subject);
        etDescription = findViewById(R.id.et_post_description);
        etAmount = findViewById(R.id.et_post_amount);
        etTokens = findViewById(R.id.et_post_tokens);
        btnEditSave = findViewById(R.id.btn_edit_save);

        // Get post ID from intent
        postId = getIntent().getStringExtra("post_id");
    }

    private void loadPostData() {
        // Load post data from intent extras
        etTitle.setText(getIntent().getStringExtra("title"));
        etSubject.setText(getIntent().getStringExtra("subject"));
        etDescription.setText(getIntent().getStringExtra("description"));
        etAmount.setText(String.valueOf(getIntent().getDoubleExtra("amount", 0.0)));
        etTokens.setText(String.valueOf(getIntent().getIntExtra("tokens", 0)));
    }

    private void enableEditing() {
        isEditing = true;
        setFieldsEnabled(true);
        btnEditSave.setText(R.string.save);

        int editableBackgroundColor = getResources().getColor(R.color.editable_background, getTheme());
        changeEditableBackgroundTint(editableBackgroundColor);
    }

    private void changeEditableBackgroundTint(int color) {
        View[] editableViews = {etTitle, etSubject, etDescription, etAmount, etTokens};

        for (View view : editableViews) {
            if (view.getParent() instanceof TextInputLayout) {
                ((TextInputLayout) view.getParent()).setBoxBackgroundColor(color);
            }
        }
    }

    private void savePost() {
        if (!validateInputs()) {
            return;
        }

        // Disable button while saving
        btnEditSave.setEnabled(false);

        // Get updated values
        String title = etTitle.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        double amount;
        int tokens;

        try {
            amount = Double.parseDouble(etAmount.getText().toString().trim());
            tokens = Integer.parseInt(etTokens.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid amount and tokens", Toast.LENGTH_SHORT).show();
            btnEditSave.setEnabled(true);
            return;
        }

        // Create update data
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("subject", subject);
        updates.put("description", description);
        updates.put("amount", amount);
        updates.put("tokens", tokens);
        updates.put("updatedAt", java.util.Calendar.getInstance().getTime());

        // Update in Firestore
        db.collection("posts")
                .document(postId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditPostActivity.this, 
                            "Post updated successfully", Toast.LENGTH_SHORT).show();
                    
                    isEditing = false;
                    setFieldsEnabled(false);
                    btnEditSave.setText(R.string.edit);
                    
                    int defaultBackgroundColor = getResources().getColor(
                            android.R.color.transparent, getTheme());
                    changeEditableBackgroundTint(defaultBackgroundColor);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating post", e);
                    Toast.makeText(EditPostActivity.this, 
                            "Error updating post: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> btnEditSave.setEnabled(true));
    }

    private boolean validateInputs() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("Title is required");
            return false;
        }
        if (etSubject.getText().toString().trim().isEmpty()) {
            etSubject.setError("Subject is required");
            return false;
        }
        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Description is required");
            return false;
        }
        if (etAmount.getText().toString().trim().isEmpty()) {
            etAmount.setError("Amount is required");
            return false;
        }
        if (etTokens.getText().toString().trim().isEmpty()) {
            etTokens.setError("Tokens are required");
            return false;
        }
        return true;
    }

    private void setFieldsEnabled(boolean enabled) {
        etTitle.setEnabled(enabled);
        etSubject.setEnabled(enabled);
        etDescription.setEnabled(enabled);
        etAmount.setEnabled(enabled);
        etTokens.setEnabled(enabled);
    }
}
