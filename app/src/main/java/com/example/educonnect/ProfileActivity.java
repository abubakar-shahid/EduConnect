package com.example.educonnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etPhoneNumber, etCity;
    private AutoCompleteTextView spinnerCountryCode, spinnerExperienceLevel, spinnerCountry;
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

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etCity = findViewById(R.id.et_city);
        spinnerCountryCode = findViewById(R.id.spinner_country_code);
        spinnerExperienceLevel = findViewById(R.id.spinner_experience_level);
        spinnerCountry = findViewById(R.id.spinner_country);
        btnEditSave = findViewById(R.id.btn_edit_save);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> countryCodeAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_codes, android.R.layout.simple_dropdown_item_1line);
        spinnerCountryCode.setAdapter(countryCodeAdapter);

        ArrayAdapter<CharSequence> experienceLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience_levels, android.R.layout.simple_dropdown_item_1line);
        spinnerExperienceLevel.setAdapter(experienceLevelAdapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_dropdown_item_1line);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void loadProfileData() {
        // TODO: Load profile data from database or shared preferences
        // For now, we'll use dummy data
        etFullName.setText("John Doe");
        etPhoneNumber.setText("1234567890");
        etCity.setText("New York");
        spinnerCountryCode.setText(spinnerCountryCode.getAdapter().getItem(0).toString(), false);
        spinnerExperienceLevel.setText(spinnerExperienceLevel.getAdapter().getItem(1).toString(), false);
        spinnerCountry.setText(spinnerCountry.getAdapter().getItem(0).toString(), false);
    }

    private void enableEditing() {
        isEditing = true;
        setFieldsEnabled(true);
        btnEditSave.setText(R.string.save);

        // Change background tint of editable fields
        int editableBackgroundColor = getResources().getColor(R.color.editable_background, getTheme());
        changeEditableBackgroundTint(editableBackgroundColor);
    }

    private void changeEditableBackgroundTint(int color) {
        View[] editableViews = {
            etFullName, etPhoneNumber, etCity,
            spinnerCountryCode, spinnerExperienceLevel, spinnerCountry
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

        // Reset background tint of editable fields
        int defaultBackgroundColor = getResources().getColor(android.R.color.transparent, getTheme());
        changeEditableBackgroundTint(defaultBackgroundColor);

        // TODO: Save profile data to database or shared preferences
    }

    private void setFieldsEnabled(boolean enabled) {
        etFullName.setEnabled(enabled);
        etPhoneNumber.setEnabled(enabled);
        etCity.setEnabled(enabled);
        spinnerCountryCode.setEnabled(enabled);
        spinnerExperienceLevel.setEnabled(enabled);
        spinnerCountry.setEnabled(enabled);
    }
}
