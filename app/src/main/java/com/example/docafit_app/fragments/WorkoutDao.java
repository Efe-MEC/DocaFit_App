package com.example.docafit_app.fragments;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    void insertWorkout(WorkoutEntry entry);

    @Update
    void updateWorkout(WorkoutEntry entry);

    @Delete
    void deleteWorkout(WorkoutEntry entry);

    @Query("SELECT * FROM workout_entries ORDER BY date DESC")
    List<WorkoutEntry> getAllWorkouts();

    @Query("SELECT * FROM workout_entries WHERE id = :id")
    WorkoutEntry getWorkoutById(int id);

    @Query("SELECT * FROM workout_entries WHERE exerciseName = :exercise ORDER BY date DESC")
    List<WorkoutEntry> getWorkoutsByExercise(String exercise);

}

