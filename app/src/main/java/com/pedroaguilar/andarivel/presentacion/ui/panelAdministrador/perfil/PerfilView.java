package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosView;

public interface PerfilView extends CamaraYpermisosView {

    void setDatosUsuarioEnUi(String nombre, String apellidos, String direccion, String telefono, String mail, StorageReference url);

    void setImagenUsuario(StorageReference url);
}
