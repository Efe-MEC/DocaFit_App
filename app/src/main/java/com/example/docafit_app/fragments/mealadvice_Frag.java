package com.example.docafit_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.docafit_app.R;

import java.util.Random;

public class mealadvice_Frag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_mealadvice, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button suggestMealBtn = view.findViewById(R.id.buttonSuggestMeal);
        TextView mealTextView = view.findViewById(R.id.textViewMeal);

        suggestMealBtn.setOnClickListener(v -> {
            String[] meals = getResources().getStringArray(R.array.meal_list);
            String randomMeal = meals[new Random().nextInt(meals.length)];

            String[] mealLines = randomMeal.split(",\\s*");

            StringBuilder displayMeal = new StringBuilder();
            for (String line : mealLines) {
                displayMeal.append("• ").append(line).append("\n");
            }

            String suggestionPrefix = getString(R.string.suggested_meal_label);
            mealTextView.setText(suggestionPrefix + ":\n\n" + displayMeal.toString().trim());
        });
    }
}
