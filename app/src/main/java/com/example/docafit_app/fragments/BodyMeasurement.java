package com.example.docafit_app.fragments;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "body_measurements")
public class BodyMeasurement {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public float weight;
    public float height;
    public float waist;
    public float hip;
    public float neck;
    public String date; // yyyy-MM-dd

    public float getWeight() {
        return weight;
    }
}

