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
        try {
            float waistNeckDiff = waist - neck;
            if (waistNeckDiff <= 0 || height <= 0) return 0f;

            float denominator = 1.0324f - 0.19077f * (float)Math.log10(waistNeckDiff)
                    + 0.15456f * (float)Math.log10(height);
            return 495f / denominator - 450f;
        } catch (Exception e) {
            return 0f;
        }
    }


    public float getWeight() {
        return weight;
    }
}

