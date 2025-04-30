package com.example.docafit_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {

    private static final String PREFS_NAME = "theme_prefs";
    private static final String THEME_KEY = "theme";

    public static void toggleTheme(Context context) {
        int currentMode = AppCompatDelegate.getDefaultNightMode();

        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        saveThemePreference(context);
    }

    private static void saveThemePreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        editor.putInt(THEME_KEY, currentMode);
        editor.apply();
    }

    public static void applySavedTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedTheme = preferences.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_NO); // varsayılan olarak gündüz modu
        AppCompatDelegate.setDefaultNightMode(savedTheme);
    }
}
