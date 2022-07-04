package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.informes;

import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

import java.util.ArrayList;

public interface InformesView extends BaseView {
    void agnadirListaUsuariosCompleta(ArrayList<Fichaje> list);
    void mostrarListaVacia();
    void mostrarFalloFirebase();
}
