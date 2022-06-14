package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.home;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

/**
 * Interfaz que sirve de "puente" y comunicacion entre el fragmento (Vista) y
 * el presentador. De manera que el presentador manda acciones a la vista
 * a traves de los metodos de esta interfaz
 */
public interface HomeView extends BaseView {
    void setEstadoInicioJornada();
    void setEstadoFinalJornada();
}
