package com.example.docafit_app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.docafit_app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class home_Frag extends Fragment {

    private LineChart chartWorkout;
    private WorkoutViewModel workoutViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_home, container, false);
        chartWorkout = rootView.findViewById(R.id.chart_workout);
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        workoutViewModel.getAllWorkouts().observe(getViewLifecycleOwner(), this::updateWorkoutChart);

        return rootView;
    }

    private void updateWorkoutChart(List<WorkoutEntry> entries) {
        LineData lineData = new LineData();
        List<Entry> squat = new ArrayList<>(), bench = new ArrayList<>(), deadlift = new ArrayList<>();
        int s = 1, b = 1, d = 1;

        for (WorkoutEntry e : entries) {
            switch (e.getExercise()) {
                case "Squat": squat.add(new Entry(s++, e.getWeight())); break;
                case "Bench Press": bench.add(new Entry(b++, e.getWeight())); break;
                case "Deadlift": deadlift.add(new Entry(d++, e.getWeight())); break;
            }
        }

        if (!squat.isEmpty()) {
            LineDataSet set = new LineDataSet(squat, "Squat");
            set.setColor(Color.RED);
            lineData.addDataSet(set);
        }
        if (!bench.isEmpty()) {
            LineDataSet set = new LineDataSet(bench, "Bench Press");
            set.setColor(Color.BLUE);
            lineData.addDataSet(set);
        }
        if (!deadlift.isEmpty()) {
            LineDataSet set = new LineDataSet(deadlift, "Deadlift");
            set.setColor(Color.GREEN);
            lineData.addDataSet(set);
        }

        chartWorkout.setData(lineData);
        chartWorkout.getDescription().setText(getString(R.string.workout_progress));
        chartWorkout.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartWorkout.getXAxis().setDrawGridLines(false);
        chartWorkout.invalidate();
    }
}
