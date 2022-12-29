package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario;

import android.content.Intent;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario
 * Create by Pedro Aguilar Fernández on 29/12/2022 at 13:08
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public interface CalendarioView extends BaseView {
    void mostrarErrorRequeridoTitle();

    void mostrarErrorRequeridoDescription();

    void mostrarToastDateRequerido();

    void lanzarCalendar(Intent intent);

}
