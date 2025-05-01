package com.example.docafit_app.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import androidx.fragment.app.Fragment;
import com.example.docafit_app.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class exerciseSuggestion_Frag extends Fragment {
    Map<String, List<String>> exercises = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_exercisesuggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Spinner setup
        Spinner spinner = view.findViewById(R.id.spinnerBolge);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.area_list,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View itemView, int position, long id) {
                String selectedRegion = parent.getItemAtPosition(position).toString();
                selectExerciseForRegion(selectedRegion, view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Button click
        Button button = view.findViewById(R.id.buttonOner);
        button.setOnClickListener(v -> {
            String selectedRegion = spinner.getSelectedItem().toString();
            selectExerciseForRegion(selectedRegion, view);
        });
    }

    private void selectExerciseForRegion(String selectedRegion, View view) {
        exercises.put("Abs", Arrays.asList("Plank", "Crunch", "Leg Raise"));
        exercises.put("Chest", Arrays.asList("Push-up", "Bench Press", "Dumbbell Fly", "Dips", "Cable Crossover"));
        exercises.put("Glutes", Arrays.asList("Squat", "Lunge", "Leg Press", "Deadlift"));
        exercises.put("Shoulder", Arrays.asList("Overhead Press", "Lateral Raise", "Rear Delt Fly", "Face Pull", "Arnold Press", "Shoulder Press"));
        exercises.put("Back", Arrays.asList("Barbell Row", "Lat Pulldown", "Pull-up", "Chin-up", "Cable Pullover", "Shrugs", "Deadlift"));
        exercises.put("Calves", Arrays.asList("Seated Calf Raise", "Standing Calf Raise", "Leg Press Calf Raise"));
        exercises.put("Ham-Strings", Arrays.asList("Seated Leg Curl", "Back Extension", "Deadlift", "Nordic Curl", "Lying Leg Curl"));
        exercises.put("Biceps", Arrays.asList("Preacher Curl", "Cable Curl", "Incline Curl", "Hammer Curl", "Standing Curl"));
        exercises.put("Triceps", Arrays.asList("Pushdown", "Skull Crusher", "Close Grip Bench", "Overhead Extension", "Kickback"));
        exercises.put("Forearms", Arrays.asList("Wrist Curl", "Wrist Extension", "Plate Hold"));

        List<String> exerciseList = exercises.get(selectedRegion);
        if (exerciseList != null && !exerciseList.isEmpty()) {
            String randomExercise = exerciseList.get(new Random().nextInt(exerciseList.size()));
            TextView textView = view.findViewById(R.id.textViewEgzersiz);
            textView.setText(randomExercise);
        }
    }
}
