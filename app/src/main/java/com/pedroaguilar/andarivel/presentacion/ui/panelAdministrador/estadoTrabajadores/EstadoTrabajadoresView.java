package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

import java.util.ArrayList;

public interface EstadoTrabajadoresView extends BaseView {
    //    String setInicioJornada();
//    String setFinalJornada();
//    void compareDate();
    void mostrarListaVacia();

    void mostrarFalloFirebase();

    void agnadirListaUsuarios(ArrayList<Fichaje> list);
}
