package com.example.shopshoes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Model.User;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private RadioButton female,male;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText name, email, pass,address;
    private AppCompatButton SignupBtn;
    private boolean gender;
    private TextView alreadyHaveAcc;
    private DatabaseReference myRootRef;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private User user;
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
        name = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signp_pass);
        address = findViewById(R.id.location_et);
        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        gender = true;
        user = new User();
    }
    private void settingUpListeners(){
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPass = pass.getText().toString().trim();
                String userAddress = address.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    name.setError("Enter Full name");
                } else if (TextUtils.isEmpty(userEmail)) {
                    email.setError("Enter email");
                }
                else if (!userEmail.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(userPass)) {
                    pass.setError("Enter pass");
                }
                else if (TextUtils.isEmpty(userAddress)) {
                    address.setError("Enter your address");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    SignupBtn.setVisibility(View.GONE);
                    user.setName(userName);
                    user.setEmail(userEmail);
                    user.setPassword(userPass);
                    user.setAddress(userAddress);
                    user.setGender(gender);
                    user.setUserType("user");
                    user.setPhotoUrl("");
                    mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUpActivity.this, "Create user success.",
                                    Toast.LENGTH_SHORT).show();
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            user.setUserId(currentUserId);
                            myRootRef.child("Users").child(currentUserId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignUpActivity.this, "welcome",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Failed to Create Account..!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            }
        });
        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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