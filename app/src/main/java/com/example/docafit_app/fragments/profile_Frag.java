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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.docafit_app.R;
import com.example.docafit_app.logIn_Act;
import com.example.docafit_app.fragments.profileUtils.editProfile_Act;
import com.example.docafit_app.utils.ThemeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class profile_Frag extends Fragment {

    private Button themeToggleButton;
    private Button languageToggleButton;
    private Button logoutButton;
    private Button editProfileButton;
    private Button deleteAccountButton;

    private TextView emailTextView;
    private TextView userTextView;
    private TextView genderTextView;

    private final HashMap<String, String> genderTranslations = new HashMap<String, String>() {{
        put("Erkek", "Male");
        put("Kadın", "Female");
        put("Diğer", "Other");
        put("Male", "Erkek");
        put("Female", "Kadın");
        put("Other", "Diğer");
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        themeToggleButton = view.findViewById(R.id.themeToggleButton);
        languageToggleButton = view.findViewById(R.id.languageToggleButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        emailTextView = view.findViewById(R.id.email);
        userTextView = view.findViewById(R.id.username);
        genderTextView = view.findViewById(R.id.gender);

        themeToggleButton.setGravity(Gravity.CENTER);
        languageToggleButton.setGravity(Gravity.CENTER);
        logoutButton.setGravity(Gravity.CENTER);
        editProfileButton.setGravity(Gravity.CENTER);
        deleteAccountButton.setGravity(Gravity.CENTER);

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

            DatabaseReference databaseRef = FirebaseDatabase
                    .getInstance("https://docafit-app-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Users")
                    .child(user.getUid());

            databaseRef.child("gender").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String gender = task.getResult().getValue(String.class);
                    if (gender != null) {
                        String currentLang = Locale.getDefault().getLanguage();
                        if (currentLang.equals("tr") && genderTranslations.containsKey(gender)) {
                            genderTextView.setText(gender.equals("Male") || gender.equals("Female") || gender.equals("Other")
                                    ? genderTranslations.get(gender) : gender);
                        } else if (currentLang.equals("en") && genderTranslations.containsKey(gender)) {
                            genderTextView.setText(gender.equals("Erkek") || gender.equals("Kadın") || gender.equals("Diğer")
                                    ? genderTranslations.get(gender) : gender);
                        } else {
                            genderTextView.setText(gender);
                        }
                    } else {
                        genderTextView.setText(getString(R.string.not_specified));
                    }
                } else {
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

        deleteAccountButton.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_account)
                    .setMessage(R.string.confirm_delete_account)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            android.widget.EditText passwordInput = new android.widget.EditText(requireContext());
                            passwordInput.setHint(R.string.password_hint);
                            passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

                            new android.app.AlertDialog.Builder(requireContext())
                                    .setTitle(R.string.password_hint)
                                    .setView(passwordInput)
                                    .setPositiveButton(R.string.confirm, (dialog1, which1) -> {
                                        String password = passwordInput.getText().toString();
                                        if (!password.isEmpty()) {
                                            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), password);

                                            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference userRef = FirebaseDatabase
                                                            .getInstance("https://docafit-app-default-rtdb.europe-west1.firebasedatabase.app")
                                                            .getReference("Users")
                                                            .child(currentUser.getUid());

                                                    userRef.removeValue().addOnCompleteListener(removeTask -> {
                                                        if (removeTask.isSuccessful()) {
                                                            currentUser.delete().addOnCompleteListener(deleteTask -> {
                                                                if (deleteTask.isSuccessful()) {
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    Intent intent = new Intent(requireActivity(), logIn_Act.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                } else {
                                                                    Log.e("DeleteAccount", "Error: " + deleteTask.getException());
                                                                    android.widget.Toast.makeText(requireContext(), R.string.account_delete_failed, android.widget.Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        } else {
                                                            Log.e("DeleteAccount", "Database error: " + removeTask.getException());
                                                            android.widget.Toast.makeText(requireContext(), R.string.account_delete_failed, android.widget.Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    Log.e("Reauthentication", "Error: " + task.getException());
                                                    android.widget.Toast.makeText(requireContext(), R.string.fail, android.widget.Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            android.widget.Toast.makeText(requireContext(), R.string.password_hint, android.widget.Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
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
