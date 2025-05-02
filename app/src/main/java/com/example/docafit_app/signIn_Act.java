package com.example.docafit_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class signIn_Act extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button signInButton;
    private ImageView logoImageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signin);

        mAuth = FirebaseAuth.getInstance();

        logoImageView = findViewById(R.id.logoImageView);
        emailEditText = findViewById(R.id.emailSignUp);
        passwordEditText = findViewById(R.id.passwordSignUp);
        nameEditText = findViewById(R.id.nameSignUp);
        signInButton = findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String username = nameEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                Toast.makeText(signIn_Act.this, getString(R.string.toast_fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(signIn_Act.this, getString(R.string.toast_password_min), Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                String defaultGender = "";

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(profileUpdates).addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(uid)
                                                .child("gender")
                                                .setValue(defaultGender)
                                                .addOnCompleteListener(genderTask -> {
                                                    Toast.makeText(signIn_Act.this, getString(R.string.toast_signup_success), Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(signIn_Act.this, logIn_Act.class));
                                                    finish();
                                                });
                                    } else {
                                        Toast.makeText(signIn_Act.this, getString(R.string.toast_signup_failed, updateTask.getException().getMessage()), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(signIn_Act.this, getString(R.string.toast_signup_failed, task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
