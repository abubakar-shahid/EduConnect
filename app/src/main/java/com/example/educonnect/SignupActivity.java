package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private TextInputEditText fullNameInput, emailInput, passwordInput, confirmPasswordInput;
    private RadioGroup userTypeRadioGroup;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        fullNameInput = findViewById(R.id.full_name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        userTypeRadioGroup = findViewById(R.id.user_type_radio_group);
        signupButton = findViewById(R.id.signup_button);
        TextView loginLink = findViewById(R.id.login_link);

        signupButton.setOnClickListener(v -> handleSignup());
        loginLink.setOnClickListener(v -> finish());
    }

    private void handleSignup() {
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Get selected user type
        int selectedId = userTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioButton = findViewById(selectedId);
        String userType = radioButton.getText().toString().toLowerCase();

        // Validate input
        if (fullName.isEmpty()) {
            fullNameInput.setError("Full name is required");
            fullNameInput.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords don't match");
            confirmPasswordInput.requestFocus();
            return;
        }

        // Disable signup button
        signupButton.setEnabled(false);

        // Create user with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success
                        String userId = mAuth.getCurrentUser().getUid();
                        
                        // Create user document in Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", fullName);
                        user.put("email", email);
                        user.put("userType", userType);
                        user.put("createdAt", FieldValue.serverTimestamp());

                        db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    // Redirect to appropriate dashboard
                                    Intent intent;
                                    if (userType.equals("student")) {
                                        intent = new Intent(SignupActivity.this, 
                                                StudentDashboardActivity.class);
                                    } else {
                                        intent = new Intent(SignupActivity.this, 
                                                TutorDashboardActivity.class);
                                    }
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error adding user data", e);
                                    Toast.makeText(SignupActivity.this, 
                                            "Error: " + e.getMessage(), 
                                            Toast.LENGTH_SHORT).show();
                                    signupButton.setEnabled(true);
                                });
                    } else {
                        // If sign up fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, 
                                "Signup failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        signupButton.setEnabled(true);
                    }
                });
    }
}
