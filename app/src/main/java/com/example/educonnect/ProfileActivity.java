package com.example.educonnect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        initViews();
        setupSpinners();
        loadProfileData();

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

    @SuppressLint("WrongViewCast")
    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etCity = findViewById(R.id.et_city);
        spinnerCountryCode = findViewById(R.id.spinner_country_code);
        spinnerExperienceLevel = findViewById(R.id.spinner_experience_level);
        spinnerCountry = findViewById(R.id.spinner_country);
//        btnEditSave = findViewById(R.id.btn_edit_save);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> countryCodeAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_codes, android.R.layout.simple_spinner_item);
        countryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountryCode.setAdapter(countryCodeAdapter);

        ArrayAdapter<CharSequence> experienceLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience_levels, android.R.layout.simple_spinner_item);
        experienceLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperienceLevel.setAdapter(experienceLevelAdapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void loadProfileData() {
        // TODO: Load profile data from database or shared preferences
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

        // TODO: Save profile data to database or shared preferences
    }
}
