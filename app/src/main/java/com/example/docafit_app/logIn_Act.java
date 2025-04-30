package com.example.docafit_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class logIn_Act extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton, forgotPasswordButton;
    private ImageView logoImageView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(logIn_Act.this, mainPage_Act.class));
            finish();
            return;
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);  // <-- eklenen buton
        logoImageView = findViewById(R.id.logoImageView);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(logIn_Act.this, getString(R.string.toast_fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(logIn_Act.this, getString(R.string.toast_logged_in, user.getEmail()), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(logIn_Act.this, mainPage_Act.class));
                            finish();
                        } else {
                            Toast.makeText(logIn_Act.this, getString(R.string.toast_login_failed, task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(logIn_Act.this, signIn_Act.class);
            startActivity(intent);
        });

        forgotPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(logIn_Act.this, getString(R.string.toast_fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(logIn_Act.this,
                                    getString(R.string.reset_email_sent, email),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(logIn_Act.this,
                                    getString(R.string.reset_email_failed, task.getException().getMessage()),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
