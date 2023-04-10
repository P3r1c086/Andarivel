package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador;

import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosView;

import java.util.ArrayList;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador
 * Create by Pedro Aguilar Fernández on 14/12/2022 at 13:48
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public interface TablonAnunciosAdminView extends CamaraYpermisosView {
    void setImagenUsuario(StorageReference url);

    void mostrarErrorRequeridoTitle();

    void mostrarExitoSubidaAnuncio();

    void mostrarError();

    void agnadirListaAnunciosCompleta(ArrayList<Anuncio> list);

    void mostrarListaVacia();
}
