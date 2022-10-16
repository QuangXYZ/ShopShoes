package com.example.shopshoes.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.shopshoes.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private RadioButton female,male;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private AppCompatButton SignupBtn;
    private boolean gender;
    private TextView alreadyHaveAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
        settingUpListeners();
    }
    private void initUI(){
        female = findViewById(R.id.female_radio);
        male = findViewById(R.id.male_radio);
        SignupBtn = findViewById(R.id.signup_btn);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.signup_progressbar);
        progressBar.setVisibility(View.GONE);
        alreadyHaveAcc = findViewById(R.id.already_have_account_btn);
        gender = true;
    }
    private void settingUpListeners(){
        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.radio_button);
                male.setTextColor(Color.parseColor("#FF000000"));
                female.setBackgroundResource(R.color.black);
                female.setTextColor(Color.parseColor("#FFFFFFFF"));

                gender = false;
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackgroundResource(R.drawable.radio_button);
                female.setTextColor(Color.parseColor("#FF000000"));
                male.setBackgroundResource(R.color.black);
                male.setTextColor(Color.parseColor("#FFFFFFFF"));
                gender = true;
            }
        });
    }
}