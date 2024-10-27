package com.example.educonnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditPostActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etSubject, etDescription, etAmount, etTokens;
    private Button btnEditSave;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

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
        isEditing = false;
        setFieldsEnabled(false);
        btnEditSave.setText(R.string.edit);

        int defaultBackgroundColor = getResources().getColor(android.R.color.transparent, getTheme());
        changeEditableBackgroundTint(defaultBackgroundColor);

        // TODO: Save updated post data
    }

    private void setFieldsEnabled(boolean enabled) {
        etTitle.setEnabled(enabled);
        etSubject.setEnabled(enabled);
        etDescription.setEnabled(enabled);
        etAmount.setEnabled(enabled);
        etTokens.setEnabled(enabled);
    }
}
