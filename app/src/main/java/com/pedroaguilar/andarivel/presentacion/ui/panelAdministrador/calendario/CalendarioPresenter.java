package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario;

import android.content.Intent;
import android.provider.CalendarContract;

import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;

import java.util.Calendar;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario
 * Create by Pedro Aguilar Fernández on 29/12/2022 at 13:08
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/

public class CalendarioPresenter extends BasePresenter<CalendarioView> {

    public void botonCrearEvento(String title, String description, Calendar date) {
        if (validarCampos(title, description, date)) {
            crearEvento(title, description, date);
        }
    }

    private void crearEvento(String title, String description, Calendar date) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTimeInMillis())
                //.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "pajaros33@gmail.com, mariaasecas@gmail.com");
        view.lanzarCalendar(intent);
    }

    private boolean validarCampos(String title, String description, Calendar date) {
        boolean resultado = true;

        if (title.isEmpty()) {
            view.mostrarErrorRequeridoTitle();
            resultado = false;
        } else if (description.isEmpty()) {
            view.mostrarErrorRequeridoDescription();
            resultado = false;
        } else if (date == null) {
            view.mostrarToastDateRequerido();
            resultado = false;
        }
        return resultado;
    }

}
