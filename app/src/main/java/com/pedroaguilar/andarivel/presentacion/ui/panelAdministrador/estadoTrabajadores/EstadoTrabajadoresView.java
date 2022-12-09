package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface EstadoTrabajadoresView extends BaseView {
    String setInicioJornada();

    String setFinalJornada();

    void compareDate();
}
