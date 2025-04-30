package com.example.docafit_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.res.Configuration;

import com.example.docafit_app.utils.ThemeUtils;

import androidx.fragment.app.Fragment;
import com.example.docafit_app.R;

public class profile_Frag extends Fragment {

    private Button themeToggleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        themeToggleButton = view.findViewById(R.id.themeToggleButton);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeToggleButton.setText("Dark");
        } else {
            themeToggleButton.setText("Light");
        }

        themeToggleButton.setOnClickListener(v -> {
            ThemeUtils.toggleTheme(requireContext());
            requireActivity().recreate();
        });

        return view;
    }
}
