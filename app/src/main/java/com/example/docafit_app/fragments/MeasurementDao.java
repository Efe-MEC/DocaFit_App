package com.example.docafit_app.fragments;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MeasurementDao {

    @Query("SELECT * FROM body_measurements")
    LiveData<List<BodyMeasurement>> getAllMeasurements();

    @Query("SELECT * FROM body_measurements WHERE id = :id LIMIT 1")
    LiveData<BodyMeasurement> getMeasurementById(int id);  // LiveData döndürüyoruz
}
