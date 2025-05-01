package com.example.docafit_app.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// ViewModelFactory sınıfı
public class MeasurementViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public MeasurementViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // MeasurementViewModel sınıfı için instance yarat
        if (modelClass.isAssignableFrom(MeasurementViewModel.class)) {
            return (T) new MeasurementViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
