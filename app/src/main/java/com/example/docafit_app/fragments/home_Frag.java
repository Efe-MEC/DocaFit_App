package com.example.docafit_app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

    private LineChart lineChart;
    private WorkoutViewModel workoutViewModel;
    private MeasurementViewModel measurementViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_home, container, false);

        // LineChart'ı layout'tan bul
        lineChart = rootView.findViewById(R.id.lineChart);

        // ViewModel'leri bağla ve gözlem başlat
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        measurementViewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);

        observeWorkoutData();
        observeMeasurementData();

        return rootView;
    }

    // Egzersiz verilerini gözlemleyin
    private void observeWorkoutData() {
        workoutViewModel.getAllWorkouts().observe(getViewLifecycleOwner(), new Observer<List<WorkoutEntry>>() {
            @Override
            public void onChanged(List<WorkoutEntry> workoutEntries) {
                // Egzersiz verileri geldiğinde çizimi güncelle
                updateChart(workoutEntries, null);
            }
        });
    }

    // Vücut ölçüleri ve yağ oranı verilerini gözlemleyin
    private void observeMeasurementData() {
        measurementViewModel.getAllMeasurements().observe(getViewLifecycleOwner(), new Observer<List<BodyMeasurement>>() {
            @Override
            public void onChanged(List<BodyMeasurement> measurements) {
                // Vücut ölçüleri geldiğinde çizimi güncelle
                updateChart(null, measurements);
            }
        });
    }

    // Hem egzersiz hem de vücut ölçüleri verilerini alarak grafiği güncelleyin
    private void updateChart(List<WorkoutEntry> workoutEntries, List<BodyMeasurement> measurements) {
        LineData lineData = new LineData();

        // Egzersiz verilerini hazırlayın (Squat, Bench Press, Deadlift)
        if (workoutEntries != null) {
            List<Entry> squatEntries = new ArrayList<>();
            List<Entry> benchEntries = new ArrayList<>();
            List<Entry> deadliftEntries = new ArrayList<>();

            int squatIndex = 1, benchIndex = 1, deadliftIndex = 1;

            for (WorkoutEntry entry : workoutEntries) {
                switch (entry.getExercise()) {
                    case "Squat":
                        squatEntries.add(new Entry(squatIndex++, entry.getWeight()));
                        break;
                    case "Bench Press":
                        benchEntries.add(new Entry(benchIndex++, entry.getWeight()));
                        break;
                    case "Deadlift":
                        deadliftEntries.add(new Entry(deadliftIndex++, entry.getWeight()));
                        break;
                }
            }

            if (!squatEntries.isEmpty()) {
                LineDataSet squatSet = new LineDataSet(squatEntries, "Squat");
                squatSet.setColor(Color.RED);
                squatSet.setCircleColor(Color.RED);
                lineData.addDataSet(squatSet);
            }

            if (!benchEntries.isEmpty()) {
                LineDataSet benchSet = new LineDataSet(benchEntries, "Bench Press");
                benchSet.setColor(Color.BLUE);
                benchSet.setCircleColor(Color.BLUE);
                lineData.addDataSet(benchSet);
            }

            if (!deadliftEntries.isEmpty()) {
                LineDataSet deadliftSet = new LineDataSet(deadliftEntries, "Deadlift");
                deadliftSet.setColor(Color.GREEN);
                deadliftSet.setCircleColor(Color.GREEN);
                lineData.addDataSet(deadliftSet);
            }
        }

        // Vücut ölçüleri ve yağ oranını ekleyin (Weight ve Body Fat)
        if (measurements != null) {
            List<Entry> weightEntries = new ArrayList<>();
            List<Entry> bodyFatEntries = new ArrayList<>();
            int index = 1;

            for (BodyMeasurement measurement : measurements) {
                weightEntries.add(new Entry(index, measurement.getWeight()));
                bodyFatEntries.add(new Entry(index++, measurement.calculateBodyFatPercentage()));
            }

            // Kilo verisini ekle
            if (!weightEntries.isEmpty()) {
                LineDataSet weightSet = new LineDataSet(weightEntries, "Weight");
                weightSet.setColor(Color.YELLOW);
                weightSet.setCircleColor(Color.YELLOW);
                lineData.addDataSet(weightSet);
            }

            // Yağ oranı verisini ekle
            if (!bodyFatEntries.isEmpty()) {
                LineDataSet bodyFatSet = new LineDataSet(bodyFatEntries, "Body Fat %");
                bodyFatSet.setColor(Color.MAGENTA);
                bodyFatSet.setCircleColor(Color.MAGENTA);
                lineData.addDataSet(bodyFatSet);
            }
        }

        // Grafik ayarlarını yap
        lineChart.setData(lineData);
        lineChart.getDescription().setText("Progress Tracking");
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.invalidate(); // Yeniden çiz
    }
}
