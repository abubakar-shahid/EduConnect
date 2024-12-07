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
import com.google.firebase.firestore.SetOptions;

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
        
        // First set the email from Firebase Auth
        if (mAuth.getCurrentUser() != null) {
            etEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        // Load basic info from users collection first
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener(userDoc -> {
                if (userDoc.exists()) {
                    String fullName = userDoc.getString("fullName");
                    if (fullName != null && !fullName.isEmpty()) {
                        etFullName.setText(fullName);
                    }
                }
                
                // Then load additional profile data if it exists
                loadAdditionalProfileData(userId);
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading basic profile", e);
                Toast.makeText(TutorProfileActivity.this,
                        "Error loading basic profile: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            });
    }

    private void loadAdditionalProfileData(String userId) {
        db.collection("tutors").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        updateUIWithProfile(documentSnapshot);
                    } else {
                        // Set placeholder texts for empty fields
//                        setPlaceholders();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading additional profile data", e);
                    Toast.makeText(TutorProfileActivity.this,
                            "Error loading profile details: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
//                    setPlaceholders();
                });
    }

    private void updateUIWithProfile(DocumentSnapshot document) {
        // Only update additional fields since name and email are already set
        String phoneNumber = document.getString("phoneNumber");
        String expertise1 = document.getString("expertise1");
        String expertise2 = document.getString("expertise2");
        String expertise3 = document.getString("expertise3");
        String city = document.getString("city");
        String countryCode = document.getString("countryCode");
        String country = document.getString("country");

        // Set only one text (either value or hint) for each field
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            etPhoneNumber.setText(phoneNumber);
            etPhoneNumber.setHint(null);
        } else {
            etPhoneNumber.setText(null);
            etPhoneNumber.setHint("Add your phone number");
        }
        
        if (expertise1 != null && !expertise1.isEmpty()) {
            etExpertise1.setText(expertise1);
            etExpertise1.setHint(null);
        } else {
            etExpertise1.setText(null);
            etExpertise1.setHint("Expertise 1");
        }
        
        if (expertise2 != null && !expertise2.isEmpty()) {
            etExpertise2.setText(expertise2);
            etExpertise2.setHint(null);
        } else {
            etExpertise2.setText(null);
            etExpertise2.setHint("Expertise 2");
        }
        
        if (expertise3 != null && !expertise3.isEmpty()) {
            etExpertise3.setText(expertise3);
            etExpertise3.setHint(null);
        } else {
            etExpertise3.setText(null);
            etExpertise3.setHint("Expertise 3");
        }
        
        if (city != null && !city.isEmpty()) {
            etCity.setText(city);
            etCity.setHint(null);
        } else {
            etCity.setText(null);
            etCity.setHint("Add your city");
        }

        if (countryCode != null && !countryCode.isEmpty()) {
            spinnerCountryCode.setText(countryCode, false);
            spinnerCountryCode.setHint(null);
        } else {
            spinnerCountryCode.setText(null, false);
            spinnerCountryCode.setHint("Country Code");
        }

        if (country != null && !country.isEmpty()) {
            spinnerCountry.setText(country, false);
            spinnerCountry.setHint(null);
        } else {
            spinnerCountry.setText(null, false);
            spinnerCountry.setHint("Select Country");
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
        // Don't enable email as it's not editable
        etEmail.setEnabled(false);
        
        // Enable/disable other fields
        etFullName.setEnabled(enabled);
        etPhoneNumber.setEnabled(enabled);
        etExpertise1.setEnabled(enabled);
        etExpertise2.setEnabled(enabled);
        etExpertise3.setEnabled(enabled);
        etCity.setEnabled(enabled);
        spinnerCountryCode.setEnabled(enabled);
        spinnerCountry.setEnabled(enabled);
    }

    private void saveProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        
        Map<String, Object> profile = new HashMap<>();
        // Only save non-empty values
        String fullName = etFullName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String expertise1 = etExpertise1.getText().toString().trim();
        String expertise2 = etExpertise2.getText().toString().trim();
        String expertise3 = etExpertise3.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String countryCode = spinnerCountryCode.getText().toString().trim();
        String country = spinnerCountry.getText().toString().trim();

        // Add fullName to profile map
        if (!fullName.isEmpty()) profile.put("fullName", fullName);
        if (!phoneNumber.isEmpty()) profile.put("phoneNumber", phoneNumber);
        if (!expertise1.isEmpty()) profile.put("expertise1", expertise1);
        if (!expertise2.isEmpty()) profile.put("expertise2", expertise2);
        if (!expertise3.isEmpty()) profile.put("expertise3", expertise3);
        if (!city.isEmpty()) profile.put("city", city);
        if (!countryCode.isEmpty()) profile.put("countryCode", countryCode);
        if (!country.isEmpty()) profile.put("country", country);
        
        profile.put("updatedAt", java.util.Calendar.getInstance().getTime());

        // First update the users collection with the fullName
        if (!fullName.isEmpty()) {
            Map<String, Object> userUpdate = new HashMap<>();
            userUpdate.put("fullName", fullName);
            
            db.collection("users").document(userId)
                .set(userUpdate, SetOptions.merge())
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating user's fullName", e);
                });
        }

        // Then update the tutors collection with all profile data
        db.collection("tutors").document(userId)
                .set(profile, SetOptions.merge())  // Use merge to preserve existing fields
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

    private void setPlaceholders() {
        // Clear all text and set hints
        etPhoneNumber.setText(null);
        etPhoneNumber.setHint("Add your phone number");

        etExpertise1.setText(null);
        etExpertise1.setHint("Expertise 1");

        etExpertise2.setText(null);
        etExpertise2.setHint("Expertise 2");

        etExpertise3.setText(null);
        etExpertise3.setHint("Expertise 3");

        etCity.setText(null);
        etCity.setHint("Add your city");

        // For spinners
        spinnerCountryCode.setText(null, false);
        spinnerCountryCode.setHint("Country Code");

        spinnerCountry.setText(null, false);
        spinnerCountry.setHint("Select Country");
    }
}
