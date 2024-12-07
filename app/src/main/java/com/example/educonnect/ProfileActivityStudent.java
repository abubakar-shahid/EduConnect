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
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivityStudent extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private TextInputEditText etFullName, etEmail, etPhoneNumber, etInstitute, etCity;
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
        
        // Load basic info from users collection first
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener(userDoc -> {
                if (userDoc.exists()) {
                    String fullName = userDoc.getString("fullName");
                    String email = userDoc.getString("email");
                    if (fullName != null && !fullName.isEmpty()) {
                        etFullName.setText(fullName);
                    }
                    if (email != null && !email.isEmpty()) {
                        etEmail.setText(email);
                    } else {
                        // Fallback to Firebase Auth email if not in Firestore
                        etEmail.setText(mAuth.getCurrentUser().getEmail());
                    }
                }
                
                // Then load additional profile data if it exists
                loadAdditionalProfileData(userId);
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading basic profile", e);
                Toast.makeText(ProfileActivityStudent.this,
                        "Error loading basic profile: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            });
    }

    private void loadAdditionalProfileData(String userId) {
        db.collection("students").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        updateUIWithProfile(documentSnapshot);
                    } else {
                        // Set placeholder texts for empty fields
                        setPlaceholders();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading additional profile data", e);
                    Toast.makeText(ProfileActivityStudent.this,
                            "Error loading profile details: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    setPlaceholders();
                });
    }

    private void setPlaceholders() {
        etPhoneNumber.setText("");
        etInstitute.setText("");
        etCity.setText("");
        spinnerCountryCode.setText("", false);
        spinnerCategory.setText("", false);
        spinnerCountry.setText("", false);

        etPhoneNumber.setHint("Add your phone number");
        etInstitute.setHint("Add your institute");
        etCity.setHint("Add your city");
        spinnerCountryCode.setHint("Select country code");
        spinnerCategory.setHint("Select education category");
        spinnerCountry.setHint("Select country");
    }

    private void updateUIWithProfile(DocumentSnapshot document) {
        // Get email from document
        String email = document.getString("email");
        if (email != null && !email.isEmpty()) {
            etEmail.setText(email);
            etEmail.setHint(null);
        } else {
            // If no email in Firestore, use Firebase Auth email
            etEmail.setText(mAuth.getCurrentUser().getEmail());
            etEmail.setHint(null);
        }

        // Get values from document for other fields
        String phoneNumber = document.getString("phoneNumber");
        String institute = document.getString("institute");
        String city = document.getString("city");
        String countryCode = document.getString("countryCode");
        String category = document.getString("category");
        String country = document.getString("country");

        // Set values or hints based on whether data exists
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            etPhoneNumber.setText(phoneNumber);
            etPhoneNumber.setHint(null);
        } else {
            etPhoneNumber.setText("");
            etPhoneNumber.setHint("Add your phone number");
        }
        
        etFullName.setText(document.getString("fullName") != null && !document.getString("fullName").isEmpty() ? document.getString("fullName") : "Add your full name");
        
        etInstitute.setText(institute != null && !institute.isEmpty() ? institute : "");
        etInstitute.setHint(institute == null || institute.isEmpty() ? "Add your institute" : "");
        
        etCity.setText(city != null && !city.isEmpty() ? city : "");
        etCity.setHint(city == null || city.isEmpty() ? "Add your city" : "");

        if (countryCode != null && !countryCode.isEmpty()) {
            spinnerCountryCode.setText(countryCode, false);
        } else {
            spinnerCountryCode.setText("", false);
            spinnerCountryCode.setHint("Country Code");
        }

        if (category != null && !category.isEmpty()) {
            spinnerCategory.setText(category, false);
        } else {
            spinnerCategory.setText("", false);
            spinnerCategory.setHint("Select Category");
        }

        if (country != null && !country.isEmpty()) {
            spinnerCountry.setText(country, false);
        } else {
            spinnerCountry.setText("", false);
            spinnerCountry.setHint("Select Country");
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
        String newEmail = etEmail.getText().toString().trim();
        
        // Check if email has been changed
        if (!newEmail.equals(mAuth.getCurrentUser().getEmail())) {
            // Update email in Firebase Auth
            mAuth.getCurrentUser().updateEmail(newEmail)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivityStudent.this, 
                        "Email updated successfully", Toast.LENGTH_SHORT).show();
                    
                    // Update email in users collection
                    Map<String, Object> userUpdate = new HashMap<>();
                    userUpdate.put("email", newEmail);
                    db.collection("users").document(userId)
                        .set(userUpdate, SetOptions.merge())
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error updating email in users collection", e);
                        });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivityStudent.this,
                        "Failed to update email. You may need to re-login: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                    // Revert the email field to current email
                    etEmail.setText(mAuth.getCurrentUser().getEmail());
                    return;
                });
        }

        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", etFullName.getText().toString().trim());
        profile.put("email", newEmail);
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
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
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
        etEmail.setEnabled(enabled);
        etPhoneNumber.setEnabled(enabled);
        etInstitute.setEnabled(enabled);
        etCity.setEnabled(enabled);
        spinnerCountryCode.setEnabled(enabled);
        spinnerCategory.setEnabled(enabled);
        spinnerCountry.setEnabled(enabled);
    }
}
