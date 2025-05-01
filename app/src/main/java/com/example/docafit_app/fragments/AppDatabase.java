package com.example.docafit_app.fragments; // NOT: fragments değil, 'database' gibi ayrı bir package daha doğru olur

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.docafit_app.fragments.BodyMeasurement;
import com.example.docafit_app.fragments.WorkoutEntry;
import com.example.docafit_app.fragments.MeasurementDao;
import com.example.docafit_app.fragments.WorkoutDao;

@Database(entities = {WorkoutEntry.class, BodyMeasurement.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract WorkoutDao workoutDao();
    public abstract MeasurementDao measurementDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "fitness_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
