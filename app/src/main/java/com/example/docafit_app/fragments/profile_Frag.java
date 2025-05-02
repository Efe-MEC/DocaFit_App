package com.example.docafit_app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.Configuration;
import android.util.Log;

import com.example.docafit_app.fragments.profileUtils.editProfile_Act;
import com.example.docafit_app.utils.ThemeUtils;

import androidx.fragment.app.Fragment;

import com.example.docafit_app.R;
import com.example.docafit_app.logIn_Act;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class profile_Frag extends Fragment {

    private Button themeToggleButton;
    private Button languageToggleButton;
    private Button logoutButton;
    private Button editProfileButton;

    private TextView emailTextView;
    private TextView userTextView;
    private TextView genderTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        themeToggleButton = view.findViewById(R.id.themeToggleButton);
        languageToggleButton = view.findViewById(R.id.languageToggleButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.edit_profile_button);

        emailTextView = view.findViewById(R.id.email);
        userTextView = view.findViewById(R.id.username);
        genderTextView = view.findViewById(R.id.gender); // Bu satır önemli

        themeToggleButton.setGravity(Gravity.CENTER);
        languageToggleButton.setGravity(Gravity.CENTER);
        logoutButton.setGravity(Gravity.CENTER);
        editProfileButton.setGravity(Gravity.CENTER);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                emailTextView.setText(email);
            }

            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                userTextView.setText(displayName);
            } else {
                userTextView.setText(getString(R.string.user_label));
            }

            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            databaseRef.child("gender").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String gender = task.getResult().getValue(String.class);
                    Log.d("GenderCheck", "Gelen gender: " + gender);

                    if (gender != null && !gender.equalsIgnoreCase(getString(R.string.choose_gender))) {
                        genderTextView.setText(gender);
                    } else {
                        genderTextView.setText(getString(R.string.not_specified));
                    }
                } else {
                    Log.e("GenderCheck", "Firebase hata: ", task.getException());
                    genderTextView.setText(getString(R.string.fail));
                }
            });
        }

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeToggleButton.setText(getString(R.string.theme_dark));
        } else {
            themeToggleButton.setText(getString(R.string.theme_light));
        }

        themeToggleButton.setOnClickListener(v -> {
            ThemeUtils.toggleTheme(requireContext());
            requireActivity().recreate();
        });

        languageToggleButton.setOnClickListener(v -> {
            String currentLanguage = Locale.getDefault().getLanguage();
            if (currentLanguage.equals("en")) {
                setLocale("tr");
            } else {
                setLocale("en");
            }
        });

        updateLanguageButtonText();

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), logIn_Act.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), editProfile_Act.class);
            startActivity(intent);
        });

        return view;
    }

    public void setLocale(String localeCode) {
        SharedPreferences preferences = requireContext().getSharedPreferences("settings", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", localeCode);
        editor.apply();

        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());

        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }

    private void updateLanguageButtonText() {
        String currentLanguage = Locale.getDefault().getLanguage();
        if (currentLanguage.equals("en")) {
            languageToggleButton.setText(getString(R.string.language_english));
        } else if (currentLanguage.equals("tr")) {
            languageToggleButton.setText(getString(R.string.language_turkish));
        }
    }
}
