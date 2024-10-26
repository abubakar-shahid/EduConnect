package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        TextView signupLink = findViewById(R.id.signup);
//        TextView forgotPasswordLink = findViewById(R.id.forgot_password);
//
//        signupLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this, SignupActivity.class));
//            }
//        });
//
//        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
//            }
//        });
    }
}
