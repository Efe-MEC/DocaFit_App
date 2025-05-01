package com.example.docafit_app.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutViewModel extends AndroidViewModel {

    private WorkoutDao workoutDao;
    private Executor executor;  // ExecutorService'i ekliyoruz

    // Constructor
    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);  // getDatabase yerine getInstance kullanıldı
        workoutDao = db.workoutDao();
        executor = Executors.newSingleThreadExecutor(); // ExecutorService'i başlatıyoruz
    }

    // Workout verilerini eklemek için kullanılan metot
    public void insertWorkout(WorkoutEntry entry) {
        executor.execute(() -> workoutDao.insertWorkout(entry));  // Arka planda veritabanına ekleme
    }

    // Belirli bir egzersiz için Workout verilerini almak
    public LiveData<List<WorkoutEntry>> getWorkouts(String exercise) {
        MutableLiveData<List<WorkoutEntry>> data = new MutableLiveData<>();

        executor.execute(() -> {
            List<WorkoutEntry> result = workoutDao.getWorkoutsByExercise(exercise);
            data.postValue(result);  // Veriyi UI thread'ine gönderiyoruz
        });

        return data;
    }

    // Tüm Workout verilerini almak
    public LiveData<List<WorkoutEntry>> getAllWorkouts() {
        MutableLiveData<List<WorkoutEntry>> data = new MutableLiveData<>();

        executor.execute(() -> {
            List<WorkoutEntry> result = workoutDao.getAllWorkouts();  // Veritabanından tüm veriyi alıyoruz
            data.postValue(result);  // Veriyi UI thread'ine gönderiyoruz
        });

        return data;
    }
}
