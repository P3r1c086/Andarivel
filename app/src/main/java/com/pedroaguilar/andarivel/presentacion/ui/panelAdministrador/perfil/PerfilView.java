package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface PerfilView extends BaseView {

    void showErrorSubirImagen();
    void setDatosUsuarioEnUi(String nombre, String apellidos, String direccion, String telefono, String mail, StorageReference url);
    void setImagenUsuario(StorageReference url);
    void navegarAlLogin();
}
