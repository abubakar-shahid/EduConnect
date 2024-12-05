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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivityStudent extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private TextInputEditText etFullName, etEmail, etPassword, etPhoneNumber, etInstitute, etCity;
    private AutoCompleteTextView spinnerCountryCode, spinnerCategory, spinnerCountry;
    private Button btnEditSave;
    private boolean isEditing = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupSpinners();
        loadProfileFromFirestore();

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    saveProfile();
                } else {
                    enableEditing();
                }
            }
        });
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etInstitute = findViewById(R.id.et_institute);
        etCity = findViewById(R.id.et_city);
        spinnerCountryCode = findViewById(R.id.spinner_country_code);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerCountry = findViewById(R.id.spinner_country);
        btnEditSave = findViewById(R.id.btn_edit_save);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> countryCodeAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_codes, android.R.layout.simple_dropdown_item_1line);
        spinnerCountryCode.setAdapter(countryCodeAdapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.education_categories, android.R.layout.simple_dropdown_item_1line);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_dropdown_item_1line);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void loadProfileFromFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("students").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        updateUIWithProfile(documentSnapshot);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading profile", e);
                    Toast.makeText(ProfileActivityStudent.this,
                            "Error loading profile: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIWithProfile(DocumentSnapshot document) {
        etFullName.setText(document.getString("fullName"));
        etEmail.setText(document.getString("email"));
        etPhoneNumber.setText(document.getString("phoneNumber"));
        etInstitute.setText(document.getString("institute"));
        etCity.setText(document.getString("city"));
        
        String countryCode = document.getString("countryCode");
        if (countryCode != null) {
            spinnerCountryCode.setText(countryCode, false);
        }
        
        String category = document.getString("category");
        if (category != null) {
            spinnerCategory.setText(category, false);
        }
        
        String country = document.getString("country");
        if (country != null) {
            spinnerCountry.setText(country, false);
        }
    }

    private void enableEditing() {
        isEditing = true;
        setFieldsEnabled(true);
        btnEditSave.setText(R.string.save);

        int editableBackgroundColor = getResources().getColor(R.color.editable_background, getTheme());
        changeEditableBackgroundTint(editableBackgroundColor);
    }

    private void changeEditableBackgroundTint(int color) {
        View[] editableViews = {
            etFullName, etPhoneNumber, etInstitute, etCity,
            spinnerCountryCode, spinnerCategory, spinnerCountry
        };

        for (View view : editableViews) {
            if (view.getParent() instanceof TextInputLayout) {
                ((TextInputLayout) view.getParent()).setBoxBackgroundColor(color);
            }
        }
    }

    private void saveProfile() {
        if (!validateInputs()) {
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", etFullName.getText().toString().trim());
        profile.put("email", etEmail.getText().toString().trim());
        profile.put("phoneNumber", etPhoneNumber.getText().toString().trim());
        profile.put("institute", etInstitute.getText().toString().trim());
        profile.put("city", etCity.getText().toString().trim());
        profile.put("countryCode", spinnerCountryCode.getText().toString());
        profile.put("category", spinnerCategory.getText().toString());
        profile.put("country", spinnerCountry.getText().toString());
        profile.put("updatedAt", java.util.Calendar.getInstance().getTime());

        // Disable button while saving
        btnEditSave.setEnabled(false);

        db.collection("students").document(userId)
                .set(profile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivityStudent.this,
                            "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    isEditing = false;
                    setFieldsEnabled(false);
                    btnEditSave.setText(R.string.edit);
                    int defaultBackgroundColor = getResources().getColor(
                            android.R.color.transparent, getTheme());
                    changeEditableBackgroundTint(defaultBackgroundColor);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating profile", e);
                    Toast.makeText(ProfileActivityStudent.this,
                            "Error updating profile: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> btnEditSave.setEnabled(true));
    }

    private boolean validateInputs() {
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Name is required");
            return false;
        }
        if (etPhoneNumber.getText().toString().trim().isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            return false;
        }
        if (etInstitute.getText().toString().trim().isEmpty()) {
            etInstitute.setError("Institute is required");
            return false;
        }
        if (spinnerCategory.getText().toString().isEmpty()) {
            spinnerCategory.setError("Category is required");
            return false;
        }
        return true;
    }

    private void setFieldsEnabled(boolean enabled) {
        etFullName.setEnabled(enabled);
        etPhoneNumber.setEnabled(enabled);
        etInstitute.setEnabled(enabled);
        etCity.setEnabled(enabled);
        spinnerCountryCode.setEnabled(enabled);
        spinnerCategory.setEnabled(enabled);
        spinnerCountry.setEnabled(enabled);
        // Email and password fields remain disabled for security
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
    }
}
