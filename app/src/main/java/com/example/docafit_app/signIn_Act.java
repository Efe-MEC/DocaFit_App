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

public class signIn_Act extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private ImageView logoImageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signin);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailSignUp);
        passwordEditText = findViewById(R.id.passwordSignUp);
        signInButton = findViewById(R.id.signUpButton);
        logoImageView = findViewById(R.id.logoImageView);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(signIn_Act.this, "Please fill every areas", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(signIn_Act.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(signIn_Act.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signIn_Act.this, logIn_Act.class));
                            finish();
                        } else {
                            Toast.makeText(signIn_Act.this, "Signing up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
