package com.example.educonnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etPhoneNumber, etCity;
    private Spinner spinnerCountryCode, spinnerExperienceLevel, spinnerCountry;
    private Button btnEditSave;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etCity = findViewById(R.id.etCity);
        spinnerCountryCode = findViewById(R.id.spinnerCountryCode);
        spinnerExperienceLevel = findViewById(R.id.spinnerExperienceLevel);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        btnEditSave = findViewById(R.id.btnEditSave);

        // Set up spinners
        setupSpinners();

        // Load user data
        loadUserData();

        btnEditSave.setOnClickListener(v -> {
            if (isEditing) {
                saveProfile();
            } else {
                enableEditing();
            }
        });
    }

    private void setupSpinners() {
        // Set up country code spinner
        ArrayAdapter<String> countryCodeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.country_codes));
        countryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountryCode.setAdapter(countryCodeAdapter);

        // Set up experience level spinner
        ArrayAdapter<String> experienceLevelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Beginner", "Intermediate", "Advanced"});
        experienceLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperienceLevel.setAdapter(experienceLevelAdapter);

        // Set up country spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.countries));
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void loadUserData() {
        // TODO: Load user data from database or shared preferences
        // For now, we'll use dummy data
        etFullName.setText("John Doe");
        etPhoneNumber.setText("1234567890");
        etCity.setText("New York");
        spinnerCountryCode.setSelection(0);
        spinnerExperienceLevel.setSelection(1);
        spinnerCountry.setSelection(0);
    }

    private void enableEditing() {
        isEditing = true;
        etFullName.setEnabled(true);
        etPhoneNumber.setEnabled(true);
        etCity.setEnabled(true);
        spinnerCountryCode.setEnabled(true);
        spinnerExperienceLevel.setEnabled(true);
        spinnerCountry.setEnabled(true);
        btnEditSave.setText(R.string.save);
    }

    private void saveProfile() {
        isEditing = false;
        etFullName.setEnabled(false);
        etPhoneNumber.setEnabled(false);
        etCity.setEnabled(false);
        spinnerCountryCode.setEnabled(false);
        spinnerExperienceLevel.setEnabled(false);
        spinnerCountry.setEnabled(false);
        btnEditSave.setText(R.string.edit);

        // TODO: Save user data to database or shared preferences
    }
}
