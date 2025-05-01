package com.example.docafit_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {
    private static final String PREFS_NAME = "settings";
    private static final String KEY_THEME = "theme_mode";

    public static void applySavedTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int themeMode = preferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(themeMode);
    }

    public static void toggleTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentMode = preferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);

        int newMode = (currentMode == AppCompatDelegate.MODE_NIGHT_YES)
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES;

        preferences.edit().putInt(KEY_THEME, newMode).apply();

        AppCompatDelegate.setDefaultNightMode(newMode);
    }
}
