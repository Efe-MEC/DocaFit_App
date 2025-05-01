package com.example.docafit_app.fragments;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Veritabanınızda yer alacak tabloları burada belirtiyoruz
@Database(entities = {BodyMeasurement.class}, version = 1, exportSchema = false)
public abstract class MeasurementDatabase extends RoomDatabase {

    // DAO sınıfınızı burada tanımlıyorsunuz
    public abstract MeasurementDao measurementDao();

    // Singleton örneği
    private static volatile MeasurementDatabase INSTANCE;

    // Veritabanı instance'ını almak için
    public static MeasurementDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MeasurementDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MeasurementDatabase.class, "measurement_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
