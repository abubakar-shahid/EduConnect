package com.example.educonnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TutorProfileActivity extends AppCompatActivity {

    private static final String TAG = "TutorProfileActivity";

    private TextInputEditText etFullName, etEmail, etPassword, etPhoneNumber;
    private TextInputEditText etExpertise1, etExpertise2, etExpertise3, etCity;
    private AutoCompleteTextView spinnerCountryCode, spinnerCountry;
    private Button btnEditSave;
    private boolean isEditMode = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupSpinners();
        loadTutorProfile();

        btnEditSave.setOnClickListener(v -> handleEditSave());
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etExpertise1 = findViewById(R.id.et_expertise1);
        etExpertise2 = findViewById(R.id.et_expertise2);
        etExpertise3 = findViewById(R.id.et_expertise3);
        etCity = findViewById(R.id.et_city);
        spinnerCountryCode = findViewById(R.id.spinner_country_code);
        spinnerCountry = findViewById(R.id.spinner_country);
        btnEditSave = findViewById(R.id.btn_edit_save);
    }

    private void setupSpinners() {
        // Setup country codes spinner
        String[] countryCodes = {"+1", "+44", "+91", "+92", "+61"}; // Add more as needed
        ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, countryCodes);
        spinnerCountryCode.setAdapter(countryCodeAdapter);

        // Setup countries spinner
        String[] countries = {"USA", "UK", "India", "Pakistan", "Australia"}; // Add more as needed
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, countries);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void loadTutorProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("tutors").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        updateUIWithProfile(documentSnapshot);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading tutor profile", e);
                    Toast.makeText(this, "Error loading profile: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIWithProfile(DocumentSnapshot document) {
        etFullName.setText(document.getString("fullName"));
        etEmail.setText(document.getString("email"));
        etPhoneNumber.setText(document.getString("phoneNumber"));
        etExpertise1.setText(document.getString("expertise1"));
        etExpertise2.setText(document.getString("expertise2"));
        etExpertise3.setText(document.getString("expertise3"));
        etCity.setText(document.getString("city"));
        
        String countryCode = document.getString("countryCode");
        if (countryCode != null) {
            spinnerCountryCode.setText(countryCode, false);
        }
        
        String country = document.getString("country");
        if (country != null) {
            spinnerCountry.setText(country, false);
        }
    }

    private void handleEditSave() {
        if (isEditMode) {
            // Save mode
            saveProfile();
            setFieldsEnabled(false);
            btnEditSave.setText(R.string.edit);
            isEditMode = false;
        } else {
            // Edit mode
            setFieldsEnabled(true);
            btnEditSave.setText(R.string.save);
            isEditMode = true;
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        etFullName.setEnabled(enabled);
        etPhoneNumber.setEnabled(enabled);
        etExpertise1.setEnabled(enabled);
        etExpertise2.setEnabled(enabled);
        etExpertise3.setEnabled(enabled);
        etCity.setEnabled(enabled);
        spinnerCountryCode.setEnabled(enabled);
        spinnerCountry.setEnabled(enabled);
        // Email and password fields remain disabled for security
    }

    private void saveProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", etFullName.getText().toString().trim());
        profile.put("email", etEmail.getText().toString().trim());
        profile.put("phoneNumber", etPhoneNumber.getText().toString().trim());
        profile.put("expertise1", etExpertise1.getText().toString().trim());
        profile.put("expertise2", etExpertise2.getText().toString().trim());
        profile.put("expertise3", etExpertise3.getText().toString().trim());
        profile.put("city", etCity.getText().toString().trim());
        profile.put("countryCode", spinnerCountryCode.getText().toString());
        profile.put("country", spinnerCountry.getText().toString());
        profile.put("updatedAt", java.util.Calendar.getInstance().getTime());

        db.collection("tutors").document(userId)
                .set(profile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TutorProfileActivity.this, 
                            "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating profile", e);
                    Toast.makeText(TutorProfileActivity.this, 
                            "Error updating profile: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
}
