package com.example.docafit_app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.res.Configuration;

import com.example.docafit_app.utils.ThemeUtils;

import androidx.fragment.app.Fragment;
import com.example.docafit_app.R;

import java.util.Locale;

public class profile_Frag extends Fragment {

    private Button themeToggleButton;
    private Button languageToggleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        themeToggleButton = view.findViewById(R.id.themeToggleButton);
        languageToggleButton = view.findViewById(R.id.languageToggleButton);

        // Mevcut tema modunu kontrol etme
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeToggleButton.setText(getString(R.string.theme_dark));
        } else {
            themeToggleButton.setText(getString(R.string.theme_light));
        }

        // Tema değiştirme butonuna tıklama
        themeToggleButton.setOnClickListener(v -> {
            ThemeUtils.toggleTheme(requireContext());
            requireActivity().recreate();
        });

        // Dil değiştirme butonuna tıklama
        languageToggleButton.setOnClickListener(v -> {
            String currentLanguage = Locale.getDefault().getLanguage();
            if (currentLanguage.equals("en")) {
                setLocale("tr");  // Türkçe'ye geçiş
            } else {
                setLocale("en");  // İngilizce'ye geçiş
            }
        });

        // Dil butonunun mevcut dilini kontrol etme ve güncelleme
        updateLanguageButtonText();

        return view;
    }

    // Dil değiştirme fonksiyonu
    public void setLocale(String localeCode) {
        // Dil tercihini SharedPreferences'a kaydetme
        SharedPreferences preferences = requireContext().getSharedPreferences("settings", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", localeCode);
        editor.apply();

        // Yeni dili ayarlama
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());

        // Dil değişikliği sonrasında uygulama yeniden başlatılabilir
        // Yeni dilin uygulandığı anı görmek için ilgili aktiviteyi yeniden başlatma
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }

    // Dil butonunun metnini güncelleme fonksiyonu
    private void updateLanguageButtonText() {
        String currentLanguage = Locale.getDefault().getLanguage();
        if (currentLanguage.equals("en")) {
            languageToggleButton.setText(getString(R.string.language_english));
        } else if (currentLanguage.equals("tr")) {
            languageToggleButton.setText(getString(R.string.language_turkish));
        }
    }
}
