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

    public float calculateBodyFatPercentage() {
        // Basit yağ oranı formülü (örn. Navy Method erkek için)
        float numerator = 495f;
        float denominator = 1.0324f - 0.19077f * (float)Math.log10(waist - neck) + 0.15456f * (float)Math.log10(height);
        return numerator / denominator - 450f;
    }

    public float getWeight() {
        return weight;
    }
}

