package com.example.docafit_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class welcome_Act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(welcome_Act.this, logIn_Act.class);
            startActivity(intent);
            finish();
        }, 1500);
    }
}
