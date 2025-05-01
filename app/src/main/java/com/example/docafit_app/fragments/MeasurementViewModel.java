package com.example.docafit_app.fragments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MeasurementViewModel extends AndroidViewModel {

    private final MeasurementDao measurementDao;
    private final LiveData<List<BodyMeasurement>> allMeasurements;

    public MeasurementViewModel(Application application) {
        super(application);
        // MeasurementDatabase sınıfını kullanarak DAO'yu alıyoruz
        MeasurementDatabase db = MeasurementDatabase.getDatabase(application);
        measurementDao = db.measurementDao();

        // LiveData kullanarak veritabanı sorgusunu arka planda çalıştırıyoruz
        allMeasurements = (LiveData<List<BodyMeasurement>>) measurementDao.getAllMeasurements();  // LiveData doğrudan DAO'dan alınır
    }

    public LiveData<List<BodyMeasurement>> getAllMeasurements() {
        return allMeasurements;  // Verileri gözlemlemek için dışarıya açıyoruz
    }

    public LiveData<BodyMeasurement> getMeasurementById(int id) {
        return measurementDao.getMeasurementById(id);  // Bu da bir LiveData döner
    }
}
