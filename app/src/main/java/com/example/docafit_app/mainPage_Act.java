package com.example.docafit_app;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.docafit_app.fragments.home_Frag;
import com.example.docafit_app.fragments.exerciseSuggestion_Frag;
import com.example.docafit_app.fragments.profile_Frag;
import com.example.docafit_app.utils.ThemeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;


public class mainPage_Act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String language = preferences.getString("language", "en"); // varsayılan İngilizce
        setAppLocale(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mainpage);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        loadFragment(new home_Frag());

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new home_Frag();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new profile_Frag();
                }
                else if (itemId == R.id.nav_suggestion) {
                    selectedFragment = new exerciseSuggestion_Frag();
                }

                return loadFragment(selectedFragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setAppLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}

