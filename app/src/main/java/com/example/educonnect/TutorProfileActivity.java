package com.example.educonnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TutorProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPassword, etPhoneNumber, etCity;
    private AutoCompleteTextView spinnerCountryCode, spinnerExpertise, spinnerCountry;
    private Button btnEditSave;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

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

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etCity = findViewById(R.id.et_city);
        spinnerCountryCode = findViewById(R.id.spinner_country_code);
        spinnerExpertise = findViewById(R.id.spinner_expertise);
        spinnerCountry = findViewById(R.id.spinner_country);
        btnEditSave = findViewById(R.id.btn_edit_save);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> countryCodeAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_codes, android.R.layout.simple_dropdown_item_1line);
        spinnerCountryCode.setAdapter(countryCodeAdapter);

        ArrayAdapter<CharSequence> expertiseAdapter = ArrayAdapter.createFromResource(this,
                R.array.expertise_areas, android.R.layout.simple_dropdown_item_1line);
        spinnerExpertise.setAdapter(expertiseAdapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_dropdown_item_1line);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void loadProfileData() {
        // TODO: Load profile data from database or shared preferences
        // For now, we'll use dummy data
        etFullName.setText("John Doe");
        etEmail.setText("johndoe@example.com");
        etPassword.setText("********");
        etPhoneNumber.setText("1234567890");
        etCity.setText("New York");
        spinnerCountryCode.setText(spinnerCountryCode.getAdapter().getItem(0).toString(), false);
        spinnerExpertise.setText(spinnerExpertise.getAdapter().getItem(1).toString(), false);
        spinnerCountry.setText(spinnerCountry.getAdapter().getItem(0).toString(), false);
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
            etFullName, etEmail, etPassword, etPhoneNumber, etCity,
            spinnerCountryCode, spinnerExpertise, spinnerCountry
        };

        for (View view : editableViews) {
            if (view.getParent() instanceof TextInputLayout) {
                ((TextInputLayout) view.getParent()).setBoxBackgroundColor(color);
            }
        }
    }

    private void saveProfile() {
        isEditing = false;
        setFieldsEnabled(false);
        btnEditSave.setText(R.string.edit);

        int defaultBackgroundColor = getResources().getColor(android.R.color.transparent, getTheme());
        changeEditableBackgroundTint(defaultBackgroundColor);

        // TODO: Save profile data to database or shared preferences
    }

    private void setFieldsEnabled(boolean enabled) {
        etFullName.setEnabled(enabled);
        etEmail.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        etPhoneNumber.setEnabled(enabled);
        etCity.setEnabled(enabled);
        spinnerCountryCode.setEnabled(enabled);
        spinnerExpertise.setEnabled(enabled);
        spinnerCountry.setEnabled(enabled);
    }
}
