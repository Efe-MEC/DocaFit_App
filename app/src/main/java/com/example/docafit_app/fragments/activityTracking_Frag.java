package com.example.docafit_app.fragments;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.docafit_app.R;


public class activityTracking_Frag extends Fragment {
    private EditText  editHeight, editNeck, editWaist, editHip, editWeightUsed, editReps;
    private Spinner spinnerExercise;
    private Button  buttonSaveSet;
    private WorkoutViewModel workoutViewModel;
    private RadioGroup radioGroupGender;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private Button buttonCalculateBodyFat;
    private TextView textViewBodyFatResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_activitytracking, container, false);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);


        editHeight = view.findViewById(R.id.editHeight);
        editNeck = view.findViewById(R.id.editNeck);
        editWaist = view.findViewById(R.id.editWaist);
        editHip = view.findViewById(R.id.editHip);
        editWeightUsed = view.findViewById(R.id.editWeightUsed);
        editReps = view.findViewById(R.id.editReps);
        spinnerExercise = view.findViewById(R.id.spinnerExercise);
        buttonSaveSet = view.findViewById(R.id.buttonSaveSet);
        buttonCalculateBodyFat = view.findViewById(R.id.buttonCalculateBodyFat);
        textViewBodyFatResult = view.findViewById(R.id.textViewBodyFatResult);

        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.exercise_list,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapter);

        buttonSaveSet.setOnClickListener(v -> saveWorkoutSet());
        buttonSaveSet.setOnClickListener(v -> saveWorkoutSet());
        buttonCalculateBodyFat.setOnClickListener(v -> calculateBodyFat());

        return view;
    }
    private void calculateBodyFat() {
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(requireContext(), getString(R.string.error_gender), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            float height = Float.parseFloat(editHeight.getText().toString());
            float neck = Float.parseFloat(editNeck.getText().toString());
            float waist = Float.parseFloat(editWaist.getText().toString());
            float hip = editHip.getText().toString().isEmpty() ? 0f : Float.parseFloat(editHip.getText().toString());

            boolean isMale = radioMale.isChecked();

            double bodyFat;
            if (isMale) {
                bodyFat = 495 / (1.0324 - 0.19077 * Math.log10(waist - neck)
                        + 0.15456 * Math.log10(height)) - 450;
            } else {
                if (hip == 0) {
                    Toast.makeText(requireContext(), getString(R.string.error_hip_female), Toast.LENGTH_SHORT).show();
                    return;
                }
                bodyFat = 495 / (1.29579 - 0.35004 * Math.log10(waist + hip - neck)
                        + 0.22100 * Math.log10(height)) - 450;
            }

            String result = String.format(getString(R.string.body_fat_result), bodyFat);
            textViewBodyFatResult.setText(result);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), getString(R.string.error_measurements), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveWorkoutSet() {
        String weightStr = editWeightUsed.getText().toString();
        String repsStr = editReps.getText().toString();
        String exercise = spinnerExercise.getSelectedItem().toString();

        if (exercise.equals(getString(R.string.choose_exercise))) {
            Toast.makeText(requireContext(), getString(R.string.error_select_exercise), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!weightStr.isEmpty() && !repsStr.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.saving_workout), Toast.LENGTH_SHORT).show();

            float weight = Float.parseFloat(weightStr);
            int reps = Integer.parseInt(repsStr);
            WorkoutEntry workoutEntry = new WorkoutEntry(exercise, weight, reps);

            workoutViewModel.insertWorkout(workoutEntry);

            Toast.makeText(requireContext(), getString(R.string.workout_saved), Toast.LENGTH_SHORT).show();

            editWeightUsed.setText("");
            editReps.setText("");
        } else {
            Toast.makeText(requireContext(), getString(R.string.error_enter_weight_reps), Toast.LENGTH_SHORT).show();
        }
    }
}
