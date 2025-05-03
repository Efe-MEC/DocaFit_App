package com.example.docafit_app.fragments;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "workout_entries")
public class WorkoutEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String exerciseName;
    public float weightUsed;
    public int reps;
    public String date; // yyyy-MM-dd

    public WorkoutEntry(String exerciseName, float weightUsed, int reps) {
        this.exerciseName = exerciseName;
        this.weightUsed =  weightUsed;
        this.reps = reps;
        this.date = getCurrentDate(); // örneğin bugünün tarihi
    }


    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    public String getExercise() {
        return exerciseName;
    }

    public float getWeight() {
        return weightUsed;
    }

    public int getReps() {
        return reps;
    }

    public String getDate() {
        return date;
    }



}

