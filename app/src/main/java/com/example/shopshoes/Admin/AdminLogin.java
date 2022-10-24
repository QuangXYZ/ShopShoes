package com.example.shopshoes.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopshoes.Model.User;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {
    private EditText email, pass;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference myRootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        initAll();
        settingUpListeners();

    }
    private void settingUpListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail)) {
                    email.setError("enter email");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(userPass)) {
                    pass.setError("enter pass");
                    pass.requestFocus();
                } else {
                    //call the signin funtion here
                    progressBar.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.GONE);
                    //Check authentication by using email and password
                    //mAuth.signOut();
                    mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                            User user =new User();
//                            user.setUserId(currentUserId);
//                            user.setName("Admin");
//                            user.setEmail(userEmail);
//                            user.setPassword(userPass);
//                            user.setAddress("none");
//                            user.setGender(true);
//                            user.setUserType("user");
//                            user.setPhotoUrl("");
//                            myRootRef.child("Admin").child(currentUserId).setValue(user);
                                myRootRef.child("Admin").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                                            Toast.makeText(AdminLogin.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            mAuth.signOut();
                                            Toast.makeText(AdminLogin.this, "This is not Admin login details", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            loginBtn.setVisibility(View.VISIBLE);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        mAuth.signOut();
                                        Toast.makeText(AdminLogin.this, "This is not Admin login details.ERROR", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        loginBtn.setVisibility(View.VISIBLE);
                                    }

                                });
                            } else {
                                Toast.makeText(AdminLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                loginBtn.setVisibility(View.VISIBLE);
                            }

                        }
                    });


                }
            }
        });
    }
    private void initAll() {
        email = findViewById(R.id.Admin_login_email);
        pass = findViewById(R.id.Admin_login_pass);
        loginBtn = findViewById(R.id.Admin_login_btn);
        progressBar = findViewById(R.id.Admin_login_progress_bar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();


    }
}