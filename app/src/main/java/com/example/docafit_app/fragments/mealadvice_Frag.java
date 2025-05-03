package com.example.docafit_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.docafit_app.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class mealadvice_Frag extends Fragment {

    private List<String> meals = Arrays.asList(
            "Grilled chicken with quinoa and steamed broccoli",
            "Oatmeal with whey protein, banana, and peanut butter",
            "Salmon with sweet potato and asparagus",
            "Whole wheat pasta with turkey meatballs and tomato sauce",
            "Tuna salad with avocado and mixed greens",
            "Egg white omelette with spinach and whole grain toast",
            "Greek yogurt with berries and granola",
            "Beef stir-fry with brown rice and vegetables",
            "Cottage cheese with almonds and sliced apple",
            "Protein smoothie with banana, almond milk, and spinach"
    );

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
            String randomMeal = meals.get(new Random().nextInt(meals.size()));
            mealTextView.setText("Suggested Meal:\n" + randomMeal);
        });
    }
}
