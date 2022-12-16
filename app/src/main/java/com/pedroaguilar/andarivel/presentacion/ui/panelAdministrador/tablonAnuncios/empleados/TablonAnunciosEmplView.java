package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados;

import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

import java.util.ArrayList;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados
 * Create by Pedro Aguilar Fernández on 15/12/2022 at 19:08
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public interface TablonAnunciosEmplView extends BaseView {
    void agnadirListaAnunciosCompleta(ArrayList<Anuncio> list);

    void mostrarListaVacia();

    void mostrarFalloFirebase();
}
