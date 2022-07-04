package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador;

import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface PanelAdministradorView extends BaseView {
    void inflarVistaSegunPerfil(Boolean esAdmin);

    void navegarAlLogin();

    void setImageHeaderDrawer(StorageReference url);

    void setTextHeaderDrawer(String nombre, String apellidos, String email);
}
