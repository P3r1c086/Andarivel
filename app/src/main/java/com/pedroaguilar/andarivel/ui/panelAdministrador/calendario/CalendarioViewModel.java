package com.pedroaguilar.andarivel.ui.panelAdministrador.calendario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class CalendarioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CalendarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Calendario de tareas");
    }

    public LiveData<String> getText() {
        return mText;
    }
}