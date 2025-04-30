package com.example.docafit_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docafit_app.utils.ThemeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class welcome_Act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String language = preferences.getString("language", "en");
        setAppLocale(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent;
            if (currentUser != null) {
                intent = new Intent(welcome_Act.this, mainPage_Act.class);
            } else {
                intent = new Intent(welcome_Act.this, logIn_Act.class);
            }

            startActivity(intent);
            finish();
        }, 500);
    }

    private void setAppLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
